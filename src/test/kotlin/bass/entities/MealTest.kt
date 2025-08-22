package bass.entities

import bass.exception.InsufficientStockException
import bass.exception.InvalidMealQuantityException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MealTest {
    @Test
    fun `subtract should reduce quantity correctly`() {
        val meal =
            MealEntity(
                name = "Test",
                quantity = 10,
                price = 100.0.toBigDecimal(),
                imageUrl = "https://niceimage",
                description = "description",
            )
        meal.subtract(3)
        assertEquals(7, meal.quantity)
    }

    @Test
    fun `subtract should throw if quantity is less than 1`() {
        val meal =
            MealEntity(
                name = "Test",
                quantity = 10,
                price = 100.0.toBigDecimal(),
                description = "description",
                imageUrl = "https://niceimage",
            )
        assertThrows<InvalidMealQuantityException> {
            meal.subtract(0)
        }
    }

    @Test
    fun `subtract should throw if subtracting more than stock`() {
        val meal =
            MealEntity(
                name = "Test",
                quantity = 5,
                imageUrl = "https://niceimage",
                description = "description",
                price = 100.0.toBigDecimal(),
            )
        assertThrows<InsufficientStockException> {
            meal.subtract(10)
        }
    }
}
