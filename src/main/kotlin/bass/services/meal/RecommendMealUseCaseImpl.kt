package bass.services.meal

import bass.controller.meal.usecase.RecommendMealUseCase
import bass.dto.meal.MealResponseDTO
import bass.exception.NotFoundException
import bass.mappers.toDTO
import bass.repositories.MemberRepository
import bass.services.recommendation.RecommendationService
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecommendMealUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val recommendationService: RecommendationService,
) : RecommendMealUseCase {
    override fun getRecommendationsForMember(
        memberId: Long,
        pageable: Pageable,
    ): List<MealResponseDTO> {
        val member =
            memberRepository.findByIdOrNull(memberId)
                ?: throw NotFoundException("Member not found")
        val meals = recommendationService.getRecommendedMeals(member, pageable)
        return meals.map { it.toDTO() }
    }

    override fun getHealthyRecommendationsForMember(memberId: Long): List<MealResponseDTO> {
        val member =
            memberRepository.findByIdOrNull(memberId)
                ?: throw NotFoundException("Member not found")
        val meals = recommendationService.getHealthyRecommendedMeals(member)
        return meals.map { it.toDTO() }
    }
}
