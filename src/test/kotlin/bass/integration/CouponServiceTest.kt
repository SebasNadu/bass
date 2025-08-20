package bass.integration

import bass.entities.AchievementEntity
import bass.entities.CouponEntity
import bass.entities.MemberEntity
import bass.enums.CouponType
import bass.repositories.AchievementRepository
import bass.repositories.CouponRepository
import bass.repositories.MemberRepository
import bass.services.coupon.CouponServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CouponServiceTest {
    @Autowired
    private lateinit var couponService: CouponServiceImpl

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var achievementRepository: AchievementRepository

    private lateinit var member: MemberEntity
    private lateinit var firstRankAchievement: AchievementEntity
    private lateinit var secondRankAchievement: AchievementEntity

    @BeforeEach
    fun setup() {
        member =
            memberRepository.save(
                MemberEntity(
                    name = "a",
                    email = "a@a.com",
                    password = "123",
                    role = MemberEntity.Role.CUSTOMER,
                )
            )!!

        firstRankAchievement =
            achievementRepository.save(
                AchievementEntity(
                    name = "First Rank",
                    strikesRequired = 1,
                    couponType = CouponType.FIRST_RANK,
                    description = "First rank achievement"
                )
            )

        secondRankAchievement =
            achievementRepository.save(
                AchievementEntity(
                    name = "Second Rank",
                    strikesRequired = 2,
                    couponType = CouponType.SECOND_RANK,
                    description = "Second rank achievement"
                )
            )
    }

    @Test
    fun `findAll should return all coupons for a given member`() {
        couponRepository.save(
            CouponEntity(
                code = "FIRST_RANK",
                member = member,
                achievement = firstRankAchievement,
                couponType = CouponType.FIRST_RANK,
                expiresAt = Instant.now().plus(7, ChronoUnit.DAYS),
            )
        )
        couponRepository.save(
            CouponEntity(
                code = "SECOND_RANK",
                member = member,
                achievement = secondRankAchievement,
                couponType = CouponType.SECOND_RANK,
                expiresAt = Instant.now().plus(7, ChronoUnit.DAYS),
            )
        )

        val coupons = couponService.findAll(member.id)

        assertThat(coupons).hasSize(2)
        assertThat(coupons.map { it.code })
            .containsExactlyInAnyOrder("FIRST_RANK", "SECOND_RANK")
    }

    @Test
    fun `validateUsability should return true for a non-expired coupon`() {
        val coupon = couponRepository.save(
            CouponEntity(
                code = "FIRST_RANK",
                member = member,
                achievement = firstRankAchievement,
                couponType = CouponType.FIRST_RANK,
                expiresAt = Instant.now().plus(7, ChronoUnit.DAYS),
            )
        )

        val isValid = couponService.validateUsability(coupon.id)

        assertThat(isValid).isTrue()
        assertThat(coupon.expiresAt).isInTheFuture
    }

    @Test
    fun `validateUsability should return false for an expired coupon`() {
        val expiredCoupon =
            couponRepository.save(
                CouponEntity(
                    code = "EXPIRED",
                    member = member,
                    achievement = firstRankAchievement,
                    couponType = CouponType.FIRST_RANK,
                    expiresAt = Instant.now().minus(1, ChronoUnit.DAYS),
                )
            )

        val isValid = couponService.validateUsability(expiredCoupon.id)
        assertThat(isValid).isFalse()
    }

    @Test
    fun `delete should remove the coupon from the database`() {
        val coupon = couponRepository.save(
            CouponEntity(
                code = "FIRST_RANK",
                member = member,
                achievement = firstRankAchievement,
                couponType = CouponType.FIRST_RANK,
                expiresAt = Instant.now().plus(7, ChronoUnit.DAYS),
            )
        )
        assertThat(couponRepository.findById(coupon.id)).isPresent

        couponService.delete(coupon.id)

        assertThat(couponRepository.findById(coupon.id)).isNotPresent
    }
}
