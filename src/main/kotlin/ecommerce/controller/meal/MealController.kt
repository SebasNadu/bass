package ecommerce.controller.meal

import ecommerce.annotation.CheckAdminOnly
import ecommerce.annotation.IgnoreCheckLogin
import ecommerce.controller.meal.usecase.CrudMealUseCase
import ecommerce.dto.MealDTO
import ecommerce.dto.MealPatchDTO
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

@RestController
class MealController(private val crudMealUseCase: CrudMealUseCase) {
    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH)
    fun getMeals(
        @PageableDefault(size = 10, sort = ["name"], direction = Sort.Direction.ASC)
        pageable: Pageable,
    ): Page<MealDTO> = crudMealUseCase.findAll(pageable)

    // TODO: create if necessary the request and response DTO to meal
    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH_ID)
    fun getMealById(
        @PathVariable id: Long,
    ): ResponseEntity<MealDTO> = ResponseEntity.ok(crudMealUseCase.findById(id))

    @CheckAdminOnly
    @PostMapping(MEAL_PATH)
    fun createMeal(
        @Valid @RequestBody productRequestDTO: MealDTO,
    ): ResponseEntity<MealDTO> {
        val saved = crudMealUseCase.save(productRequestDTO)
        return ResponseEntity.created(URI.create("$MEAL_PATH/${saved.id}")).body(saved)
    }

    @CheckAdminOnly
    @PutMapping(MEAL_PATH_ID)
    fun updateMealById(
        @Valid @RequestBody productDTO: MealDTO,
        @PathVariable id: Long,
    ): ResponseEntity<MealDTO> = ResponseEntity.ok(crudMealUseCase.updateById(id, productDTO))

    @CheckAdminOnly
    @PatchMapping(MEAL_PATH_ID)
    fun patchMealById(
        @Valid @RequestBody productPatchDTO: MealPatchDTO,
        @PathVariable id: Long,
    ): ResponseEntity<MealDTO> = ResponseEntity.ok(crudMealUseCase.patchById(id, productPatchDTO))

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
