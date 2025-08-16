package ecommerce.dto

import java.time.LocalDateTime

class CouponDTO(
    val name: String,
    val discountRate: String,
    val expiresAt: LocalDateTime,
    val id: Long
)
