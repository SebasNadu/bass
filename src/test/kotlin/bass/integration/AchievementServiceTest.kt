package bass.integration

import bass.entities.AchievementEntity
import bass.entities.MemberEntity
import bass.enums.CouponType
import bass.repositories.AchievementRepository
import bass.repositories.CouponRepository
import bass.repositories.MemberRepository
import bass.services.achievement.AchievementService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Transactional
@SpringBootTest
class AchievementServiceTest {
    @Autowired
    private lateinit var achievementService: AchievementService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var achievementRepository: AchievementRepository

    @Autowired
    private lateinit var couponRepository: CouponRepository

    private lateinit var member: MemberEntity
    private lateinit var couponAchievement: AchievementEntity
    private lateinit var nonCouponAchievement: AchievementEntity

    @BeforeEach
    fun setup() {
        member =
            memberRepository.save(
                MemberEntity(
                    name = "Test User",
                    email = "test@example.com",
                    password = "123",
                    role = MemberEntity.Role.CUSTOMER,
                ),
            )!!

        couponAchievement =
            achievementRepository.save(
                AchievementEntity(
                    name = "Coupon Achievement",
                    streaksRequired = 1,
                    couponType = CouponType.FIRST_RANK,
                    description = "Generates a coupon",
                ),
            )

        nonCouponAchievement =
            achievementRepository.save(
                AchievementEntity(
                    name = "Non-Coupon Achievement",
                    streaksRequired = 2,
                    couponType = null,
                    description = "Does not generate a coupon",
                ),
            )
    }

    @Test
    fun `awardAchievement generates coupon if achievement has couponType`() {
        val coupon = achievementService.awardAchievement(member.id, couponAchievement.id)

        assertThat(coupon).isNotNull
        assertThat(coupon!!.member.id).isEqualTo(member.id)
        assertThat(coupon.achievement.id).isEqualTo(couponAchievement.id)
        assertThat(coupon.couponType).isEqualTo(CouponType.FIRST_RANK)
        assertThat(coupon.code).startsWith(CouponType.FIRST_RANK.name)
        assertThat(coupon.expiresAt).isAfter(Instant.now())

        val persisted = couponRepository.findById(coupon.id)
        assertThat(persisted).isPresent
    }

    @Test
    fun `awardAchievement does not generate coupon if achievement has no couponType`() {
        val coupon = achievementService.awardAchievement(member.id, nonCouponAchievement.id)
        assertThat(coupon).isNull()

        val allCoupons = couponRepository.findAll()
        assertThat(allCoupons).isEmpty()
    }

    @Test
    fun `awardAchievement adds achievement to member`() {
        achievementService.awardAchievement(member.id, couponAchievement.id)
        val updatedMember = memberRepository.findById(member.id).get()
        assertThat(updatedMember.achievements).contains(couponAchievement)
    }
}
