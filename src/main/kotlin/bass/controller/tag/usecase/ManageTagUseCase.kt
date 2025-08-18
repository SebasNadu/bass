package bass.controller.tag.usecase

import bass.dto.tag.TagDTO
import bass.entities.TagEntity

interface ManageTagUseCase {
    fun findAll(): List<TagDTO>

    fun findByName(name: TagEntity.TagNames): TagEntity
}
