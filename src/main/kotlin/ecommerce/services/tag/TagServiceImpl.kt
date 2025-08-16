package ecommerce.services.tag

import ecommerce.controller.tag.usecase.ManageTagUseCase
import ecommerce.dto.tag.TagDTO
import ecommerce.entities.TagEntity
import ecommerce.exception.NotFoundException
import ecommerce.mappers.toDTO
import ecommerce.repositories.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagServiceImpl(
    private val tagRepository: TagRepository,
) : ManageTagUseCase {
    @Transactional(readOnly = true)
    override fun findAll(): List<TagDTO> {
        return tagRepository.findAll().map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun findByName(name: TagEntity.TagNames): TagEntity {
        return tagRepository.findByName(name) ?: throw NotFoundException("Tag with name: $name not found")
    }
}
