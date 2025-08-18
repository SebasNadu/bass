package bass.mappers

import bass.dto.tag.TagDTO
import bass.entities.TagEntity

fun TagEntity.toDTO(): TagDTO =
    TagDTO(
        name = name,
        id = this.id,
    )
