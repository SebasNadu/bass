package bass.services.order

import bass.controller.order.usecase.OrderCreationUseCase
import bass.dto.member.MemberLoginDTO
import bass.dto.OrderDTO
import bass.dto.PaymentDTO
import bass.entities.CartItemEntity
import bass.entities.MemberEntity
import bass.entities.OrderEntity
import bass.entities.OrderItemEntity
import bass.entities.PaymentEntity
import bass.exception.NotFoundException
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.model.PaymentRequest
import bass.model.StripeResponse
import bass.repositories.CartItemRepository
import bass.repositories.MemberRepository
import bass.repositories.OrderRepository
import bass.services.payment.PaymentService
import bass.services.stripe.StripeService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderServiceImpl(
    private val stripeService: StripeService,
    private val cartItemRepository: CartItemRepository,
    private val memberRepository: MemberRepository,
    private val orderRepository: OrderRepository,
    private val paymentService: PaymentService,
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

        val stripeResponse =
            stripeService.createPaymentIntent(
                paymentRequest.copy(
                    amount = cartItems.sumOf { it.totalPrice },
                ),
            )

        val order = buildOrderEntity(member, cartItems, stripeResponse)
        val savedOrder = orderRepository.save(order)
        val paymentDTO = paymentService.createPayment(savedOrder, stripeResponse)

        processPaymentOutcome(paymentDTO, cartItems)
        return savedOrder.toDTO()
    }

    private fun buildOrderEntity(
        member: MemberEntity,
        cartItems: List<CartItemEntity>,
        stripeResponse: StripeResponse,
    ): OrderEntity {
        val order =
            OrderEntity(
                status = OrderEntity.OrderStatus.CREATED,
                totalAmount = stripeResponse.amount!!,
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
