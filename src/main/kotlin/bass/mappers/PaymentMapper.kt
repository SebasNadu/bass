package bass.mappers

import bass.dto.PaymentDTO
import bass.entities.PaymentEntity
import java.math.BigDecimal

fun PaymentEntity.toDTO(
    discount: BigDecimal,
    initialAmount: BigDecimal,
): PaymentDTO {
    return PaymentDTO(
        id = this.id,
        stripePaymentIntentId = this.stripePaymentIntentId,
        amount = this.amount,
        currency = this.currency,
        status = this.status,
        failureCode = this.failureCode,
        failureMessage = this.failureMessage,
        orderId = this.order.id!!,
        discountAmount = discount,
        amountWithoutDiscount = initialAmount,
    )
}
