package ecommerce.entities

import bass.enums.CouponType
import bass.enums.DiscountType
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class CouponTypeTest {

    @Test
    fun `percentage discount returns discountValue as amount`() {
        val type = CouponType.FIRST_RANK
        assertEquals(DiscountType.PERCENTAGE, type.discountType)
        assertEquals(BigDecimal(5), type.discountAmount)
        assertEquals(30, type.validityDays)
    }

    @Test
    fun `fixed amount converts cents to dollars`() {
        val type = CouponType.WELCOME_BONUS
        assertEquals(DiscountType.FIXED_AMOUNT, type.discountType)
        assertEquals(BigDecimal("5.00"), type.discountAmount) // 500 cents -> 5.00
        assertEquals(7, type.validityDays)
    }

    @Test
    fun `free shipping always returns zero discount amount`() {
        val type = CouponType.FREE_SHIPPING
        assertEquals(DiscountType.FREE_SHIPPING, type.discountType)
        assertEquals(BigDecimal.ZERO, type.discountAmount)
        assertEquals(14, type.validityDays)
    }

    @Test
    fun `different ranks have increasing discount percentages`() {
        assertEquals(BigDecimal(5), CouponType.FIRST_RANK.discountAmount)
        assertEquals(BigDecimal(10), CouponType.SECOND_RANK.discountAmount)
        assertEquals(BigDecimal(15), CouponType.THIRD_RANK.discountAmount)
        assertEquals(BigDecimal(20), CouponType.FOURTH_RANK.discountAmount)
    }
}
