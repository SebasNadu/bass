package bass.controller.meal

import bass.annotation.CheckAdminOnly
import bass.annotation.IgnoreCheckLogin
import bass.annotation.LoginMember
import bass.controller.meal.usecase.AISearchUseCase
import bass.controller.meal.usecase.CrudMealUseCase
import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.meal.MealPatchDTO
import bass.dto.meal.MealRequestDTO
import bass.dto.meal.MealResponseDTO
import bass.dto.member.MemberLoginDTO
import bass.dto.naturalSearch.NaturalSearchRequestDTO
import bass.dto.naturalSearch.NaturalSearchResponseDTO
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.services.recommendation.RecommendationService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class MealController(
    private val crudMealUseCase: CrudMealUseCase,
    private val recommendationService: RecommendationService,
    private val crudMemberUseCase: CrudMemberUseCase,
    private val aiSearchUseCase: AISearchUseCase,
) {
    @PostMapping(MEAL_PATH_NATURAL_SEARCH)
    fun naturalSearch(
        @Valid @RequestBody request: NaturalSearchRequestDTO,
    ): ResponseEntity<NaturalSearchResponseDTO> {
        val response: NaturalSearchResponseDTO = aiSearchUseCase.naturalSearch(request)
        return ResponseEntity.ok(response)
    }

    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH)
    fun getMeals(
        @PageableDefault(size = 10, sort = ["name"], direction = Sort.Direction.ASC)
        pageable: Pageable,
    ): Page<MealResponseDTO> = crudMealUseCase.findAll(pageable)

    @GetMapping(MEAL_PATH_RECOMMENDATIONS)
    fun getRecommendations(
        @LoginMember member: MemberLoginDTO,
    ): List<MealResponseDTO> {
        val member =
            crudMemberUseCase.findById(member.id).toEntity()
        return if (recommendationService.isFreedomDay(member)) {
            recommendationService.getRecommendedMeals(member, PageRequest.of(0, 10))
        } else {
            recommendationService.getHealthyRecommendedMeals(member)
        }.map { it.toDTO() }
    }

    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH_ID)
    fun getMealById(
        @PathVariable id: Long,
    ): ResponseEntity<MealResponseDTO> = ResponseEntity.ok(crudMealUseCase.findById(id))

    @IgnoreCheckLogin
    @GetMapping(MEAL_PATH_TAG)
    fun getMealsByTag(
        @RequestParam tagName: String,
    ): ResponseEntity<List<MealResponseDTO>> = ResponseEntity.ok(crudMealUseCase.findAllByTag(tagName))

    @CheckAdminOnly
    @PostMapping(MEAL_PATH)
    fun createMeal(
        @Valid @RequestBody mealRequestDTO: MealRequestDTO,
    ): ResponseEntity<MealResponseDTO> {
        val saved = crudMealUseCase.save(mealRequestDTO)
        return ResponseEntity.created(URI.create("$MEAL_PATH/${saved.id}")).body(saved)
    }

    @CheckAdminOnly
    @PutMapping(MEAL_PATH_ID)
    fun updateMealById(
        @Valid @RequestBody mealRequestDTO: MealRequestDTO,
        @PathVariable id: Long,
    ): ResponseEntity<MealResponseDTO> = ResponseEntity.ok(crudMealUseCase.updateById(id, mealRequestDTO))

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
        const val MEAL_PATH_TAG = "$MEAL_PATH/tag"
        const val MEAL_PATH_RECOMMENDATIONS = "$MEAL_PATH/recommendations"
        const val MEAL_PATH_NATURAL_SEARCH = "$MEAL_PATH/search/natural"
    }
}
