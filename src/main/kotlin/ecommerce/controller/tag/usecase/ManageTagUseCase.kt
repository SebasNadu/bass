package ecommerce.controller.tag.usecase

import ecommerce.dto.tag.TagDTO
import ecommerce.entities.TagEntity

interface ManageTagUseCase {
    fun findAll(): List<TagDTO>

    fun findByName(name: TagEntity.TagNames): TagEntity
}
