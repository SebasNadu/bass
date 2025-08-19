package bass.controller.meal

import bass.annotation.CheckAdminOnly
import bass.annotation.IgnoreCheckLogin
import bass.controller.meal.usecase.CrudMealUseCase
import bass.dto.meal.MealDTO
import bass.dto.meal.MealPatchDTO
import bass.dto.meal.MealResponseDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

// TODO fix pagination
@RestController
class MealController(private val crudMealUseCase: CrudMealUseCase) {
    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH)
    fun getMeals(
        @PageableDefault(size = 10, sort = ["name"], direction = Sort.Direction.ASC)
        pageable: Pageable,
    ): Page<MealResponseDTO> = crudMealUseCase.findAll(pageable)

    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH_ID)
    fun getMealById(
        @PathVariable id: Long,
    ): ResponseEntity<MealResponseDTO> = ResponseEntity.ok(crudMealUseCase.findById(id))

    @CheckAdminOnly
    @PostMapping(MEAL_PATH)
    fun createMeal(
        @Valid @RequestBody mealRequestDTO: MealDTO,
    ): ResponseEntity<MealResponseDTO> {
        val saved = crudMealUseCase.save(mealRequestDTO)
        return ResponseEntity.created(URI.create("$MEAL_PATH/${saved.id}")).body(saved)
    }

    @CheckAdminOnly
    @PutMapping(MEAL_PATH_ID)
    fun updateMealById(
        @Valid @RequestBody mealDTO: MealDTO,
        @PathVariable id: Long,
    ): ResponseEntity<MealResponseDTO> = ResponseEntity.ok(crudMealUseCase.updateById(id, mealDTO))

    @CheckAdminOnly
    @PatchMapping(MEAL_PATH_ID)
    fun patchMealById(
        @Valid @RequestBody mealPatchDTO: MealPatchDTO,
        @PathVariable id: Long,
    ): ResponseEntity<MealResponseDTO> = ResponseEntity.ok(crudMealUseCase.patchById(id, mealPatchDTO))

    @CheckAdminOnly
    @DeleteMapping(MEAL_PATH_ID)
    fun deleteMealById(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        crudMealUseCase.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @CheckAdminOnly
    @DeleteMapping(MEAL_PATH)
    fun deleteAllMeals(): ResponseEntity<String> {
        crudMealUseCase.deleteAll()
        return ResponseEntity.noContent().build()
    }

    companion object {
        const val MEAL_PATH = "/api/meals"
        const val MEAL_PATH_ID = "$MEAL_PATH/{id}"
    }
}
