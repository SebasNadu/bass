package bass.services.recommendation

import bass.entities.MealEntity
import bass.entities.MemberEntity
import bass.entities.OrderEntity
import bass.entities.TagEntity
import bass.exception.NoDaysSetException
import bass.repositories.DayRepository
import bass.repositories.MealRepository
import bass.repositories.MemberRepository
import bass.repositories.OrderRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@Service
class RecommendationService(
    private val orderRepository: OrderRepository,
    private val mealRepository: MealRepository,
    private val dayRepository: DayRepository,
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun recommendedMeals(memberId: Long): List<MealEntity> {
        val member =
            memberRepository.findByIdOrNull(memberId)!!
        return if (isFreedomDay(member)) {
            getRecommendedMeals(member, PageRequest.of(0, 10))
        } else {
            getHealthyRecommendedMeals(member)
        }
    }

    @Transactional(readOnly = true)
    fun getRecommendedMeals(
        member: MemberEntity,
        pageable: Pageable,
    ): List<MealEntity> {
        val since = Instant.now().minus(DAYS_TO_SUBTRACT.toLong(), ChronoUnit.DAYS)
        val topMeals = orderRepository.findTopOrderedMealsSince(since, PageRequest.ofSize(100))
        val preferredTags = extractTagNames(member.tags)

        val personalized =
            if (preferredTags.isNotEmpty()) {
                applyRecommendationRule8020(topMeals, preferredTags)
            } else {
                emptyList()
            }

        if (personalized.isNotEmpty()) return personalized

        val fallbackRecommendations = orderRepository.findTopOrderedMealsSince(since, pageable)
        if (fallbackRecommendations.isNotEmpty()) return fallbackRecommendations

        return mealRepository.findAll().shuffled().take(pageable.pageSize)
    }

    @Transactional(readOnly = true)
    fun getHealthyRecommendedMeals(member: MemberEntity): List<MealEntity> {
        val since = Instant.now().minus(DAYS_TO_SUBTRACT.toLong(), ChronoUnit.DAYS)

        val topHealthyMeals =
            orderRepository.findTopOrderedMealsByTagSince(
                since,
                listOf("Healthy"),
                Pageable.ofSize(25),
            )

        val recentOrders =
            orderRepository.findRecentOrdersByMember(
                member.id,
                Pageable.ofSize(LAST_ORDERS_TO_EXCLUDE),
            )

        val filteredMeals = excludeRecentlyOrderedMeals(topHealthyMeals, recentOrders)

        val memberPreferredTags = extractTagNames(member.tags)
        val membersTagsFromOrders = mealRepository.findTagsByMemberId(member.id).map { it.name }.toSet()
        val combinedPreferredTags = memberPreferredTags + membersTagsFromOrders

        val personalized =
            if (combinedPreferredTags.isNotEmpty()) {
                applyRecommendationRule8020(filteredMeals, combinedPreferredTags)
            } else {
                emptyList()
            }

        if (personalized.isNotEmpty()) return personalized
        if (filteredMeals.isNotEmpty()) return filteredMeals
        if (topHealthyMeals.isNotEmpty()) return topHealthyMeals

        val healthyMeals = mealRepository.findByTagsName("Healthy")
        if (healthyMeals.isNotEmpty()) return healthyMeals

        return mealRepository.findAll().shuffled().take(10)
    }

    fun isFreedomDay(member: MemberEntity): Boolean {
        val today = Instant.now().atZone(ZoneId.systemDefault()).dayOfWeek
        val membersDays = dayRepository.findByMemberId(member.id)
        if (membersDays.isEmpty()) {
            throw NoDaysSetException()
        }
        return membersDays.any { it.dayName.name == today.toString() }
    }

    private fun applyRecommendationRule8020(
        topMeals: List<MealEntity>,
        preferredTags: Set<String>,
    ): List<MealEntity> {
        val recommendedMeals = mutableListOf<MealEntity>()
        val otherMeals = mutableListOf<MealEntity>()

        for (meal in topMeals) {
            if (meal.tags.any { it.name in preferredTags }) {
                recommendedMeals.add(meal)
            } else {
                otherMeals.add(meal)
            }
        }

        val recommendedMealsCount = (topMeals.size * 0.8).toInt().coerceAtMost(recommendedMeals.size)
        val otherMealsCount = topMeals.size - recommendedMealsCount

        return recommendedMeals.take(recommendedMealsCount) + otherMeals.take(otherMealsCount)
    }

    fun excludeRecentlyOrderedMeals(
        meals: List<MealEntity>,
        recentOrders: List<OrderEntity>,
    ): List<MealEntity> {
        val recentMealIds = recentOrders.flatMap { it.items }.map { it.meal.id }.toSet()
        return meals.filter { it.id !in recentMealIds }
    }

    private fun extractTagNames(tags: Collection<TagEntity>): Set<String> = tags.map { it.name }.toSet()

    companion object {
        const val DAYS_TO_SUBTRACT = 30
        const val LAST_ORDERS_TO_EXCLUDE = 3
    }
}
