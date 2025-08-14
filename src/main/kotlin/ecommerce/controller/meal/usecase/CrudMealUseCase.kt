package ecommerce.controller.meal.usecase

import ecommerce.dto.MealDTO
import ecommerce.dto.MealPatchDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CrudMealUseCase {
    fun findAll(pageable: Pageable): Page<MealDTO>

    fun findById(id: Long): MealDTO

    fun save(mealDTO: MealDTO): MealDTO

    fun updateById(
        id: Long,
        mealDTO: MealDTO,
    ): MealDTO

    fun patchById(
        id: Long,
        mealPatchDTO: MealPatchDTO,
    ): MealDTO

    fun deleteById(id: Long)

    fun deleteAll()

    fun validateMealNameUniqueness(name: String)
}
