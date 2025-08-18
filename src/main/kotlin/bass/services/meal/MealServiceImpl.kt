package bass.services.meal

import bass.controller.meal.usecase.CrudMealUseCase
import bass.dto.MealDTO
import bass.dto.MealPatchDTO
import bass.exception.NotFoundException
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.repositories.MealRepository
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
) : CrudMealUseCase {
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<MealDTO> {
        val meals = mealRepository.findAll(pageable)
        val mealDTOs = meals.map { it.toDTO() }
        return mealDTOs
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): MealDTO {
        val meal =
            mealRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Meal with id=$id not found")
        return meal.toDTO()
    }

    @Transactional
    override fun save(mealDTO: MealDTO): MealDTO {
        validateMealNameUniqueness(mealDTO.name)

        val meal = mealDTO.toEntity()

        // TODO: save tags, refactor following code from options to tags
//        mealDTO.options.forEach { optionDTO ->
//            val option = optionDTO.toEntity(meal)
//            meal.addOption(option)
//        }

        val savedMeal = mealRepository.save(meal)
        return savedMeal.toDTO()
    }

    @Transactional
    override fun updateById(
        id: Long,
        mealDTO: MealDTO,
    ): MealDTO {
        val existing =
            mealRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Meal with id=$id not found")

        if (existing.name != mealDTO.name) {
            validateMealNameUniqueness(mealDTO.name)
        }

        // TODO: copy tags into existing
        existing.copyFrom(mealDTO)

        return mealRepository.save(existing).toDTO()
    }

    @Transactional
    override fun patchById(
        id: Long,
        mealPatchDTO: MealPatchDTO,
    ): MealDTO {
        val existing =
            mealRepository.findByIdOrNull(id)
                ?: throw NotFoundException("Meal with id=$id not found")
        if (mealPatchDTO.name != null && existing.name != mealPatchDTO.name) {
            validateMealNameUniqueness(mealPatchDTO.name!!)
        }

        // TODO: copy tags into existing
        existing.copyFrom(mealPatchDTO)
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

    override fun findByTag(tag: String): List<MealDTO> {
        return mealRepository.findByTagsName(tag).map { it.toDTO() }
    }
}
