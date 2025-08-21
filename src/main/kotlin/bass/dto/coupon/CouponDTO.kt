package bass.dto.coupon

import bass.enums.DiscountType
import java.math.BigDecimal
import java.time.Instant

data class CouponDTO(
    val code: String,
    val displayName: String,
    val discountType: DiscountType,
    val discountValue: Int,
    val discountAmount: BigDecimal,
    val validityDays: Long,
    val isValid: Boolean,
    val expiresAt: Instant? = null,
    var id: Long = 0L,
)
