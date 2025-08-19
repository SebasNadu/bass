package bass.controller.meal.usecase

import bass.dto.meal.MealDTO
import bass.dto.meal.MealPatchDTO
import bass.dto.meal.MealResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CrudMealUseCase {
    fun findAll(pageable: Pageable): Page<MealResponseDTO>

    fun findById(id: Long): MealResponseDTO

    fun save(mealDTO: MealDTO): MealResponseDTO

    fun updateById(
        id: Long,
        mealDTO: MealDTO,
    ): MealResponseDTO

    fun patchById(
        id: Long,
        mealPatchDTO: MealPatchDTO,
    ): MealResponseDTO

    fun deleteById(id: Long)

    fun deleteAll()

    fun validateMealNameUniqueness(name: String)

    fun findByTag(tag: String): List<MealResponseDTO>
}
