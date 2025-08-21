package bass.model

import java.math.BigDecimal
import java.math.RoundingMode

data class StripeResponse(
    val id: String?,
    val amount: Long?,
    val currency: String?,
    val status: String?,
    val cancellationReason: String?,
    val clientSecret: String?,
    val customer: String?,
    val failureCode: String? = null,
    val failureMessage: String? = null,
    val created: String?,
    val paymentMethod: String?,
    val latestCharge: String?,
) {
    val amountDecimal: BigDecimal
        get() =
            amount?.toBigDecimal()?.divide(
                BigDecimal("100"), 2, RoundingMode.HALF_UP,
            ) ?: BigDecimal.ZERO
}
