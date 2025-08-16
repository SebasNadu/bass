package ecommerce.dto

import java.time.LocalDateTime

data class CouponDTO(
    val name: String,
    val memberId: Long,
    val discountRate: String? = null,
    val expiresAt: LocalDateTime? = null,
    var id: Long = 0L,
)
