package ecommerce.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CouponTest {

    @Test
    fun `should create a new coupon`() {
        val newMember = MemberEntity(
            name = "New member",
            email = "newmember@gmail.com",
            password = "lalala"
        )

        val coupon =
            CouponEntity(
                name = "First Rank discount",
                discountRate = CouponEntity.DiscountRate.FIVE_PERCENT,
                member = newMember,
            )
        println("Coupon creation: ${coupon.createdAt}")
        println("Coupon expiration: ${coupon.expiresAt}")
        assertEquals("First Rank discount", coupon.name)
        assertEquals(CouponEntity.DiscountRate.FIVE_PERCENT, coupon.discountRate)
        assertEquals("New member", coupon.member.name)
    }
}
