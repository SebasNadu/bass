package bass.mappers

import bass.dto.order.OrderItemDTO
import bass.entities.OrderItemEntity

fun OrderItemEntity.toDTO() =
    OrderItemDTO(
        mealId = meal.id,
        quantity = quantity,
        price = price,
        mealName = meal.name,
        imageUrl = meal.imageUrl,
        id = id,
    )
