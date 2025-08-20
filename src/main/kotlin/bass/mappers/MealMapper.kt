package bass.mappers

import bass.dto.meal.MealRequestDTO
import bass.dto.meal.MealResponseDTO
import bass.entities.MealEntity

fun MealEntity.toDTO(): MealResponseDTO =
    MealResponseDTO(
        name = name,
        quantity = quantity,
        price = price,
        description = description,
        imageUrl = imageUrl,
        tags = tags.map { it.toDTO() }.toSet(),
        id = id,
    )

fun MealRequestDTO.toEntity(): MealEntity =
    MealEntity(
        name = name,
        quantity = quantity,
        price = price,
        id = id,
        description = description,
        imageUrl = imageUrl,
    )
