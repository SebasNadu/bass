package bass.integration

import bass.controller.meal.usecase.CrudMealUseCase
import bass.dto.MealDTO
import bass.dto.MealPatchDTO
import bass.exception.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class MealServiceTest(
    @param:Autowired val mealService: CrudMealUseCase,
) {
    private lateinit var meal: MealDTO

    @BeforeEach
    fun setup() {
        meal =
            MealDTO(
                name = "Test Product",
                price = 19.99,
                imageUrl = "https://example.com/test.png",
                quantity = 4,
            )
    }

    @Test
    fun `should save meal`() {
        val saved = mealService.save(meal)

        assertThat(saved.id).isNotNull()
        assertThat(saved.name).isEqualTo(meal.name)
        assertThat(saved.price).isEqualTo(meal.price)
        assertThat(saved.imageUrl).isEqualTo(meal.imageUrl)
    }

    @Test
    fun `should retrieve meal by id`() {
        val saved = mealService.save(meal)
        val found = mealService.findById(saved.id)

        assertThat(found.name).isEqualTo(saved.name)
        assertThat(found.price).isEqualTo(saved.price)
    }

    @Test
    fun `should update meal by id`() {
        val saved = mealService.save(meal)
        val updated = saved.copy(name = "Updated", price = 49.99)

        val request =
            MealDTO(
                updated.name,
                price = updated.price,
                imageUrl = updated.imageUrl,
                quantity = 4,
            )
        val result = mealService.updateById(saved.id, request)

        assertThat(result.name).isEqualTo("Updated")
        assertThat(result.price).isEqualTo(49.99)
    }

    @Test
    fun `should patch meal`() {
        val saved = mealService.save(meal)
        val patch = MealPatchDTO(name = "Patched Name")

        val result = mealService.patchById(saved.id, patch)

        assertThat(result.name).isEqualTo("Patched Name")
        assertThat(result.price).isEqualTo(saved.price)
    }

    @Test
    fun `should throw when saving meal with duplicate name`() {
        mealService.save(meal)

        val ex =
            assertThrows<RuntimeException> {
                mealService.save(meal.copy(imageUrl = "https://different.com"))
            }

        assertThat(ex.message).contains("already exists")
    }

    @Test
    fun `should return all meals`() {
        mealService.save(meal)
        mealService.save(meal.copy(name = "Second Product"))
        val sortedByName: Pageable =
            PageRequest.of(0, 999, Sort.by("name"))
        val all = mealService.findAll(sortedByName)

        assertThat(all).hasSize(27)
    }

    @Test
    fun `should return paginated meals`() {
        val pageable = PageRequest.of(0, 4) // page index is 0-based
        val page = mealService.findAll(pageable)

        assertThat(page.content).hasSize(4)
        assertThat(page.totalElements).isEqualTo(25)
    }

    @Test
    fun `should delete meal by id`() {
        val saved = mealService.save(meal)

        mealService.deleteById(saved.id)

        assertThrows<NotFoundException> { mealService.findById(saved.id) }
    }

    @Test
    fun `should delete all meals`() {
        mealService.save(meal)
        mealService.save(meal.copy(name = "Another"))

        mealService.deleteAll()

        val sortedByName: Pageable =
            PageRequest.of(0, 999, Sort.by("name"))
        assertThat(mealService.findAll(sortedByName)).isEmpty()
    }

    @Test
    fun `should throw when retrieving nonexistent meal`() {
        val ex =
            assertThrows<RuntimeException> {
                mealService.findById(999L)
            }

        assertThat(ex.message).contains("Meal with id=")
    }
}
