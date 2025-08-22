package bass.controller.meal.usecase

import bass.dto.meal.MealPatchDTO
import bass.dto.meal.MealRequestDTO
import bass.dto.meal.MealResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CrudMealUseCase {
    fun findAll(pageable: Pageable): Page<MealResponseDTO>

    fun findById(id: Long): MealResponseDTO

    fun save(mealRequestDTO: MealRequestDTO): MealResponseDTO

    fun updateById(
        id: Long,
        mealRequestDTO: MealRequestDTO,
    ): MealResponseDTO

    fun patchById(
        id: Long,
        mealPatchDTO: MealPatchDTO,
    ): MealResponseDTO

    fun deleteById(id: Long)

    fun deleteAll()

    fun validateMealNameUniqueness(name: String)

    fun findAllByTag(tag: String): List<MealResponseDTO>
}
