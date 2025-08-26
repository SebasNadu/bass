package bass.controller.meal.usecase

import bass.dto.meal.MealResponseDTO
import org.springframework.data.domain.Pageable

interface RecommendMealUseCase {
    fun getRecommendationsForMember(
        memberId: Long,
        pageable: Pageable,
    ): List<MealResponseDTO>

    fun getHealthyRecommendationsForMember(memberId: Long): List<MealResponseDTO>
}
