package bass.controller.tag.usecase

import bass.dto.tag.CreateTagDTO
import bass.dto.tag.TagDTO
import bass.entities.TagEntity

interface ManageTagUseCase {
    fun findAll(): List<TagDTO>

    fun findByName(name: String): TagEntity?

    fun create(tag: CreateTagDTO): TagDTO
}
