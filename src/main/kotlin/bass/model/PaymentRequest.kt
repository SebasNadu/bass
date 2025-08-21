package bass.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.math.RoundingMode

data class PaymentRequest(
    @field:Positive
    var amount: BigDecimal,
    @field:NotBlank
    val currency: String,
    @field:NotBlank
    val paymentMethod: String,
) {
    val amountInSmallestUnit: Long
        get() {
            val smallestUnit = amount.multiply(BigDecimal("100"))
            return smallestUnit.setScale(0, RoundingMode.HALF_UP).toLong()
        }
}
