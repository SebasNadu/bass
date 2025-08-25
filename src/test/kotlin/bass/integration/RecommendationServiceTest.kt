package bass.integration

import bass.entities.MealEntity
import bass.repositories.MealRepository
import bass.repositories.MemberRepository
import bass.repositories.OrderRepository
import bass.repositories.TagRepository
import bass.services.recommendation.RecommendationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class RecommendationServiceTest {
    @Autowired
    private lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var recommendationService: RecommendationService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var mealRepository: MealRepository

    // lateinit var member: MemberEntity
    lateinit var meal: MealEntity

    @BeforeEach
    fun setup() {
//        val preferredTag1 = tagRepository.save(TagEntity(name = "salad"))
//        val preferredTag2 = tagRepository.save(TagEntity(name = "soup"))
//        tagRepository.save(TagEntity(name = "burger"))
//        tagRepository.save(TagEntity(name = "cheeseburger"))
//
//        MealEntity(
//            name = "Greek Salad",
//            quantity = 10,
//            price = 9.toBigDecimal(),
//            description = "Salad is a greek Salad",
//            imageUrl = "https:/salad.jpg",
//            tags = mutableSetOf(preferredTag1, preferredTag2)
//        )
    }

    @Test
    fun `getRecommendedMeals should return meals matching member's preferrences`() {
        val member = memberRepository.findAll().first()
        // val meals = mealRepository.findAll()
        val recommendedMeals =
            recommendationService.getRecommendedMeals(member = member, pageable = PageRequest.of(0, 10))
        println("### member tags:")
        member.tags.forEach { tag -> println("- $tag") }
        println("### Recommended Meals:")
        recommendedMeals.forEach { println("Meal: ${it.name}, Tags: ${it.tags.map { t -> t.name }}") }


        assertThat(recommendedMeals.isNotEmpty())
        assertTrue(
            recommendedMeals.any { meal -> meal.tags.any { it.name in member.tags.map { tag -> tag.name } } },
            "Recommended meals should contain member's preferred tags"
        )
    }
}
