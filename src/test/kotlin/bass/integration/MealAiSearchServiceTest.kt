package bass.integration

import bass.dto.naturalSearch.NaturalSearchRequestDTO
import bass.dto.tag.TagInferenceResultDTO
import bass.entities.MealEntity
import bass.entities.TagEntity
import bass.infrastructure.ai.TagInferenceClient
import bass.repositories.MealRepository
import bass.repositories.TagRepository
import bass.services.meal.MealServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MealAiSearchServiceTest {
    private lateinit var mealRepository: MealRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var tagInferenceClient: TagInferenceClient
    private lateinit var mealService: MealServiceImpl

    @BeforeEach
    fun setUp() {
        mealRepository = mockk(relaxed = true)
        tagRepository = mockk(relaxed = true)
        tagInferenceClient = mockk()
        mealService = MealServiceImpl(mealRepository, tagRepository, tagInferenceClient)
    }

    @Test
    fun `should return meals that match ANY of the inferred tags`() {
        // given
        val allowedTags = setOf("vegan", "spicy")
        every { tagRepository.findAll() } returns
                listOf(TagEntity(name = "vegan"), TagEntity(name = "spicy"))
        every { tagInferenceClient.inferTags("vegan food", allowedTags, 8) } returns
                TagInferenceResultDTO(listOf("vegan"))

        val meal =
            MealEntity(
                id = 1L,
                name = "Spicy Vegan Curry",
                quantity = 10,
                price = BigDecimal("12.50"),
                imageUrl = "http://example.com/curry.jpg",
                description = "Hot and tasty",
            )

        every { mealRepository.findAnyTag(listOf("vegan")) } returns listOf(meal)

        val request = NaturalSearchRequestDTO(userText = "vegan food")

        // when
        val response = mealService.naturalSearch(request)

        // then
        assertEquals(listOf("vegan"), response.selectedTags)
        assertTrue(response.meals.any { it.name == "Spicy Vegan Curry" })

        verify(exactly = 1) { tagInferenceClient.inferTags("vegan food", allowedTags, 8) }
        verify(exactly = 1) { mealRepository.findAnyTag(listOf("vegan")) }
    }

    @Test
    fun `should return meals that match ALL inferred tags`() {
        // given
        val allowedTags = setOf("vegan", "spicy")
        every { tagRepository.findAll() } returns
                listOf(TagEntity(name = "vegan"), TagEntity(name = "spicy"))
        every { tagInferenceClient.inferTags("spicy vegan", allowedTags, 8) } returns
                TagInferenceResultDTO(listOf("vegan", "spicy"))

        val meal =
            MealEntity(
                id = 2L,
                name = "Vegan Spicy Soup",
                quantity = 5,
                price = BigDecimal("15.00"),
                imageUrl = "http://example.com/soup.jpg",
                description = "Nice soup",
            )

        every { mealRepository.findAllTags(listOf("vegan", "spicy"), 2) } returns listOf(meal)

        val request = NaturalSearchRequestDTO(userText = "spicy vegan", requireAllTags = true)

        // when
        val response = mealService.naturalSearch(request)

        // then
        assertEquals(listOf("vegan", "spicy"), response.selectedTags)
        assertTrue(response.meals.any { it.name == "Vegan Spicy Soup" })

        verify(exactly = 1) { mealRepository.findAllTags(listOf("vegan", "spicy"), 2) }
    }

    @Test
    fun `should return empty when no tags are inferred`() {
        // given
        val allowedTags = emptySet<String>()
        every { tagRepository.findAll() } returns emptyList()
        every { tagInferenceClient.inferTags("random text", allowedTags, 8) } returns
                TagInferenceResultDTO(emptyList())

        val request = NaturalSearchRequestDTO(userText = "random text")

        // when
        val response = mealService.naturalSearch(request)

        // then
        assertTrue(response.selectedTags.isEmpty())
        assertTrue(response.meals.isEmpty())

        verify(exactly = 1) { tagInferenceClient.inferTags("random text", allowedTags, 8) }
    }
}
