package bass.entities

import bass.enums.CouponType
import bass.exception.CouponAlreadyUsedException
import bass.exception.CouponExpiredException
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

class CouponTest {
    private val member =
        MemberEntity(
            name = "John Doe",
            email = "john@example.com",
            password = "secret",
            role = MemberEntity.Role.CUSTOMER,
        )
    private val achievement =
        AchievementEntity(
            name = "First Streak",
            description = "Unlocked after 5 days",
            streaksRequired = 5,
            imageUrl = "http://example.com/image.png",
            couponType = CouponType.FIRST_RANK,
        )

    @Test
    fun `coupon should be valid when not expired and not used`() {
        val coupon = achievement.createCouponFor(member)

        assertTrue(coupon.isValid())
        assertFalse(coupon.isExpired())
        assertFalse(coupon.isUsed)
    }

    @Test
    fun `coupon should be expired when past expiresAt`() {
        val coupon =
            CouponEntity(
                code = "EXPIRE_ME",
                member = member,
                achievement = achievement,
                couponType = CouponType.FIRST_RANK,
                expiresAt = Instant.now().minus(1, ChronoUnit.DAYS),
            )

        assertTrue(coupon.isExpired())
        assertFalse(coupon.isValid())
    }

    @Test
    fun `markAsUsed should set isUsed and usedAt`() {
        val coupon = achievement.createCouponFor(member)

        coupon.markAsUsed()

        assertTrue(coupon.isUsed)
        assertNotNull(coupon.usedAt)
    }

    @Test
    fun `markAsUsed should throw when coupon already used`() {
        val coupon = achievement.createCouponFor(member)
        coupon.markAsUsed()

        val exception =
            assertThrows<CouponAlreadyUsedException> {
                coupon.markAsUsed()
            }

        assertEquals("Coupon with code=${coupon.code} is already used", exception.message)
    }

    @Test
    fun `markAsUsed should throw when coupon is expired`() {
        val coupon =
            CouponEntity(
                code = "EXPIRE_ME",
                member = member,
                achievement = achievement,
                couponType = CouponType.FIRST_RANK,
                expiresAt = Instant.now().minus(1, ChronoUnit.DAYS),
            )
        val exception =
            assertThrows<CouponExpiredException> {
                coupon.markAsUsed()
            }

        assertEquals("Coupon with code=${coupon.code} is expired", exception.message)
    }
}
