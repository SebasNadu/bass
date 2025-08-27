package bass.services.order

import bass.controller.order.usecase.OrderCreationUseCase
import bass.dto.member.MemberLoginDTO
import bass.dto.order.OrderDTO
import bass.dto.order.OrderResponseDTO
import bass.dto.payment.PaymentDTO
import bass.entities.CartItemEntity
import bass.entities.CouponEntity
import bass.entities.MemberEntity
import bass.entities.OrderEntity
import bass.entities.OrderItemEntity
import bass.entities.PaymentEntity
import bass.enums.DiscountType
import bass.events.OrderCompletionEvent
import bass.exception.NotFoundException
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toOrderResponseDTO
import bass.model.PaymentRequest
import bass.model.StripeResponse
import bass.repositories.CartItemRepository
import bass.repositories.CouponRepository
import bass.repositories.MemberRepository
import bass.repositories.OrderRepository
import bass.services.payment.PaymentService
import bass.services.stripe.StripeService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class OrderServiceImpl(
    private val stripeService: StripeService,
    private val cartItemRepository: CartItemRepository,
    private val memberRepository: MemberRepository,
    private val orderRepository: OrderRepository,
    private val paymentService: PaymentService,
    private val eventPublisher: ApplicationEventPublisher,
    private val couponRepository: CouponRepository,
) : OrderCreationUseCase {
    @Transactional
    override fun create(
        memberLoginDTO: MemberLoginDTO,
        paymentRequest: PaymentRequest,
    ): OrderDTO {
        val member =
            memberRepository.findByIdOrNull(memberLoginDTO.id)
                ?: throw NotFoundException("Member not found")
        val cartItems = cartItemRepository.findByMemberId(member.id)
        if (cartItems.isEmpty()) throw OperationFailedException("Cart is empty")

        validateStock(cartItems)

        val totalWithoutCoupon = cartItems.sumOf { it.totalPrice }
        val coupon = paymentRequest.couponId?.let { getAndValidateCoupon(it, member.id) }
        val (totalAmount, discountAmount) =
            coupon?.let {
                calculateTotalDiscount(totalWithoutCoupon, it)
            } ?: Pair(totalWithoutCoupon, BigDecimal.ZERO)

        val stripeResponse =
            stripeService.createPaymentIntent(
                paymentRequest.copy(
                    amount = totalAmount,
                ),
            )

        val order = buildOrderEntity(member, cartItems, stripeResponse)
        val savedOrder = orderRepository.save(order)
        val paymentDTO =
            paymentService.createPayment(
                savedOrder,
                stripeResponse,
                discountAmount,
                totalWithoutCoupon,
            )

        processPaymentOutcome(paymentDTO, cartItems)

        if (paymentDTO.status == PaymentEntity.PaymentStatus.SUCCEEDED) {
            coupon?.let {
                it.markAsUsed()
                couponRepository.save(it)
            }
            eventPublisher.publishEvent(OrderCompletionEvent(this, savedOrder))
        }
        return savedOrder.toDTO(listOf(paymentDTO))
    }

    override fun getOrdersByMemberId(
        memberId: Long,
        pageable: Pageable,
    ): List<OrderResponseDTO> {
        val orders = orderRepository.findAllByMemberId(memberId, pageable)
        return orders.map { toOrderResponseDTO(it) }
    }

    private fun calculateTotalDiscount(
        total: BigDecimal,
        coupon: CouponEntity,
    ): Pair<BigDecimal, BigDecimal> {
        val discount =
            when (coupon.discountType) {
                DiscountType.PERCENTAGE -> total.multiply(BigDecimal(coupon.discountValue).divide(BigDecimal(100)))
                DiscountType.FIXED_AMOUNT -> coupon.discountAmount
                else -> BigDecimal.ZERO
            }
        val totalWithDiscount = total.subtract(discount)
        val finalTotal = if (totalWithDiscount < BigDecimal.ZERO) BigDecimal.ZERO else totalWithDiscount

        return Pair(finalTotal, discount)
    }

    private fun getAndValidateCoupon(
        couponId: Long,
        memberId: Long,
    ): CouponEntity {
        val coupon =
            couponRepository.findByIdOrNull(couponId)
                ?: throw NotFoundException("Coupon not found")

        if (!coupon.isValid()) {
            throw OperationFailedException("Coupon with code=${coupon.code} not valid")
        }

        if (coupon.member.id != memberId) {
            throw OperationFailedException("This coupon does not belong to you.")
        }
        return coupon
    }

    private fun buildOrderEntity(
        member: MemberEntity,
        cartItems: List<CartItemEntity>,
        stripeResponse: StripeResponse,
    ): OrderEntity {
        val order =
            OrderEntity(
                status = OrderEntity.OrderStatus.CREATED,
                totalAmount = stripeResponse.amountDecimal,
                member = member,
            )
        val orderItems = cartItems.map { OrderItemEntity(order, it.meal, it.quantity, it.meal.price) }
        order.addAllItems(orderItems)
        return order
    }

    private fun processPaymentOutcome(
        payment: PaymentDTO,
        cartItems: List<CartItemEntity>,
    ) {
        when (payment.status) {
            PaymentEntity.PaymentStatus.SUCCEEDED -> {
                decreaseOptionStock(cartItems)
                cartItemRepository.deleteAll(cartItems)
            }

            PaymentEntity.PaymentStatus.PROCESSING -> decreaseOptionStock(cartItems)
            else -> return
        }
    }

    private fun decreaseOptionStock(cartItems: List<CartItemEntity>) {
        cartItems.forEach { it.meal.subtract(it.quantity) }
    }

    private fun validateStock(cartItems: List<CartItemEntity>) {
        cartItems.forEach { it.meal.validateStock(it.quantity) }
    }
}
