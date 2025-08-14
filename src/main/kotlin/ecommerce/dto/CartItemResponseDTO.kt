package ecommerce.dto

import java.time.LocalDateTime

data class CartItemResponseDTO(
    val id: Long,
    val memberId: Long,
    val meal: MealDTO,
    val quantity: Int,
    val addedAt: LocalDateTime,
)
