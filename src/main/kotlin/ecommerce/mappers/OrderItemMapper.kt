package ecommerce.mappers

import ecommerce.dto.OrderItemDTO
import ecommerce.entities.OrderItemEntity

fun OrderItemEntity.toDTO() =
    OrderItemDTO(
        mealId = meal.id,
        quantity = quantity,
        price = price,
        id = id,
    )
