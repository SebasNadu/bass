package bass.dto

import bass.dto.meal.MealResponseDTO
import java.time.LocalDateTime

data class CartItemResponseDTO(
    val id: Long,
    val memberId: Long,
    val meal: MealResponseDTO,
    val quantity: Int,
    val addedAt: LocalDateTime,
)
