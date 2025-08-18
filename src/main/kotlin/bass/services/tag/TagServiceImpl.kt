package bass.services.tag

import bass.controller.tag.usecase.ManageTagUseCase
import bass.dto.tag.TagDTO
import bass.entities.TagEntity
import bass.exception.NotFoundException
import bass.mappers.toDTO
import bass.repositories.TagRepository
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
