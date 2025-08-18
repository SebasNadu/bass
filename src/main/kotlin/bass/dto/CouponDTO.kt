package bass.dto

import java.time.Instant

data class CouponDTO(
    val name: String,
    val memberId: Long,
    val discountRate: String? = null,
    val expiresAt: Instant? = null,
    var id: Long = 0L,
)
