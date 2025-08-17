package bass.mappers

import bass.dto.OrderItemDTO
import bass.entities.OrderItemEntity

fun OrderItemEntity.toDTO() =
    OrderItemDTO(
        mealId = meal.id,
        quantity = quantity,
        price = price,
        id = id,
    )
