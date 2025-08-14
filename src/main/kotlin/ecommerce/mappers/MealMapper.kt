package ecommerce.mappers

import ecommerce.dto.MealDTO
import ecommerce.entities.MealEntity

fun MealEntity.toDTO(): MealDTO = MealDTO(name = name, quantity = quantity, price = price, imageUrl = imageUrl, id = id)

fun MealDTO.toEntity(): MealEntity =
    MealEntity(
        name = name,
        quantity = quantity,
        price = price,
        id = id,
        imageUrl = imageUrl,
    )
