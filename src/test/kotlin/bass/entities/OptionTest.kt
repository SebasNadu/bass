package bass.entities

import bass.exception.InsufficientStockException
import bass.exception.InvalidOptionQuantityException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OptionTest {
    @Test
    fun `subtract should reduce quantity correctly`() {
        val meal =
            MealEntity(
                name = "Test",
                quantity = 10,
                price = 100.0,
                imageUrl = "https://niceimage",
            )
        meal.subtract(3)
        assertEquals(7, meal.quantity)
    }

    @Test
    fun `subtract should throw if quantity is less than 1`() {
        val meal = MealEntity(name = "Test", quantity = 10, price = 100.0, imageUrl = "https://niceimage")
        assertThrows<InvalidOptionQuantityException> {
            meal.subtract(0)
        }
    }

    @Test
    fun `subtract should throw if subtracting more than stock`() {
        val meal = MealEntity(name = "Test", quantity = 5, imageUrl = "https://niceimage", price = 100.0)
        assertThrows<InsufficientStockException> {
            meal.subtract(10)
        }
    }
}
