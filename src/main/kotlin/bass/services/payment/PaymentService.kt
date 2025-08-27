package bass.services.payment

import bass.dto.payment.PaymentDTO
import bass.entities.OrderEntity
import bass.entities.PaymentEntity
import bass.mappers.toDTO
import bass.model.StripeResponse
import bass.repositories.PaymentRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun createPayment(
        order: OrderEntity,
        stripeResponse: StripeResponse,
        discount: BigDecimal,
        amountWithoutDiscount: BigDecimal,
    ): PaymentDTO {
        val payment =
            PaymentEntity(
                stripePaymentIntentId = stripeResponse.id!!,
                amount = stripeResponse.amountDecimal,
                currency = stripeResponse.currency ?: "EUR",
                status = PaymentEntity.PaymentStatus.fromValue(stripeResponse.status!!),
                failureCode = stripeResponse.failureCode,
                failureMessage = stripeResponse.failureMessage,
                order = order,
            )
        return paymentRepository.save(payment).toDTO(discount, amountWithoutDiscount)
    }
}
