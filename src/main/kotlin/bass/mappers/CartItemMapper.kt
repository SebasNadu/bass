package bass.mappers

import bass.dto.CartItemResponseDTO
import bass.entities.CartItemEntity

fun CartItemEntity.toDTO() = CartItemResponseDTO(id, member.id, meal.toDTO(), quantity, addedAt)
