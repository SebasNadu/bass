package ecommerce.mappers

import ecommerce.dto.tag.TagDTO
import ecommerce.entities.TagEntity

fun TagEntity.toDTO(): TagDTO =
    TagDTO(
        name = name.toString(),
        id = this.id,
    )
