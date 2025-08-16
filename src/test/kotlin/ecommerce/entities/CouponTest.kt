package ecommerce.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CouponTest {
    @Test
    fun `should create a new coupon`() {
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val coupon =
            CouponEntity(
                name = "FIRST_RANK",
                discountRate = CouponEntity.DiscountRate.FIVE_PERCENT,
                member = newMember,
            )
        assertEquals("FIRST_RANK", coupon.name)
        assertEquals(CouponEntity.DiscountRate.FIVE_PERCENT, coupon.discountRate)
        assertEquals("New member", coupon.member.name)
    }
}
