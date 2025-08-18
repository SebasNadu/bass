package bass.mappers

import bass.dto.MealDTO
import bass.entities.MealEntity

fun MealEntity.toDTO(): MealDTO =
    MealDTO(
        name = name,
        quantity = quantity,
        price = price,
        description = description,
        imageUrl = imageUrl,
        id = id,
    )

fun MealDTO.toEntity(): MealEntity =
    MealEntity(
        name = name,
        quantity = quantity,
        price = price,
        id = id,
        description = description,
        imageUrl = imageUrl,
    )
