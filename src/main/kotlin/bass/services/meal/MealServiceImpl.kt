package bass.services.meal

import bass.controller.meal.usecase.CrudMealUseCase
import bass.dto.meal.MealDTO
import bass.dto.meal.MealPatchDTO
import bass.dto.meal.MealResponseDTO
import bass.entities.MealEntity
import bass.exception.NotFoundException
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.repositories.MealRepository
import bass.repositories.TagRepository
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// this was ProductServiceImpl
@Service
@Primary
class MealServiceImpl(
    private val mealRepository: MealRepository,
    private val tagRepository: TagRepository,
) : CrudMealUseCase {
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<MealResponseDTO> {
        val meals = mealRepository.findAll(pageable)
        val mealDTOs = meals.map { it.toDTO() }
        return mealDTOs
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): MealResponseDTO {
        val meal =
            mealRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Meal with id=$id not found")
        return meal.toDTO()
    }

    @Transactional
    override fun save(mealDTO: MealDTO): MealResponseDTO {
        validateMealNameUniqueness(mealDTO.name)
        val meal = mealDTO.toEntity()
        addTags(meal, mealDTO.tagsIds)
        val savedMeal = mealRepository.save(meal)
        return savedMeal.toDTO()
    }

    @Transactional
    override fun updateById(
        id: Long,
        mealDTO: MealDTO,
    ): MealResponseDTO {
        val existing =
            mealRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Meal with id=$id not found")

        if (existing.name != mealDTO.name) {
            validateMealNameUniqueness(mealDTO.name)
        }
        existing.copyFrom(mealDTO)
        if (mealDTO.tagsIds.isNotEmpty()) {
            existing.clearTags()
            addTags(existing, mealDTO.tagsIds)
        }
        return mealRepository.save(existing).toDTO()
    }

    @Transactional
    override fun patchById(
        id: Long,
        mealPatchDTO: MealPatchDTO,
    ): MealResponseDTO {
        val existing =
            mealRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Meal with id=$id not found")
        if (mealPatchDTO.name != null && existing.name != mealPatchDTO.name) {
            validateMealNameUniqueness(mealPatchDTO.name!!)
        }
        existing.copyFrom(mealPatchDTO)
        if (mealPatchDTO.tagsIds.isNotEmpty()) {
            existing.clearTags()
            addTags(existing, mealPatchDTO.tagsIds)
        }
        return mealRepository.save(existing).toDTO()
    }

    @Transactional
    override fun deleteById(id: Long) {
        if (!mealRepository.existsById(id)) throw NotFoundException("Meal with ID $id not found")
        mealRepository.deleteById(id)
    }

    @Transactional
    override fun deleteAll() {
        mealRepository.deleteAll()
    }

    override fun validateMealNameUniqueness(name: String) {
        if (mealRepository.existsByName(name)) {
            throw OperationFailedException("Meal with name '$name' already exists")
        }
    }

    override fun findByTag(tag: String): List<MealResponseDTO> {
        return mealRepository.findByTagsName(tag).map { it.toDTO() }
    }

    private fun addTags(
        meal: MealEntity,
        tagsIds: Set<Long>,
    ) {
        tagsIds.forEach { id ->
            val tag =
                tagRepository.findByIdOrNull(id)
                    ?: throw NotFoundException("Tag with id=$id not found")
            meal.addTag(tag)
        }
    }
}
