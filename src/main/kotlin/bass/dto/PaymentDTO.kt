package bass.dto

import bass.entities.PaymentEntity
import java.math.BigDecimal

class PaymentDTO(
    val id: Long? = null,
    val stripePaymentIntentId: String,
    val amount: BigDecimal,
    val currency: String,
    val status: PaymentEntity.PaymentStatus,
    val failureCode: String? = null,
    val failureMessage: String? = null,
    val orderId: Long,
)
