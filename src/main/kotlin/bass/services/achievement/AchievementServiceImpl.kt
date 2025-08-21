package bass.services.achievement

import bass.entities.AchievementEntity
import bass.entities.CouponEntity
import bass.entities.MemberEntity
import bass.repositories.AchievementRepository
import bass.repositories.CouponRepository
import bass.repositories.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class AchievementServiceImpl(
    private val memberRepository: MemberRepository,
    private val achievementRepository: AchievementRepository,
    private val couponRepository: CouponRepository,
) : AchievementService {
    @Transactional
    override fun awardAchievement(
        memberId: Long,
        achievementId: Long,
    ): CouponEntity? {
        val member = memberRepository.findById(memberId).orElseThrow()
        val achievement = achievementRepository.findById(achievementId).orElseThrow()

        member.achievements.add(achievement)
        memberRepository.save(member)

        return if (achievement.generatesCoupon()) {
            generateCoupon(member, achievement)
        } else {
            null
        }
    }

    private fun generateCoupon(
        member: MemberEntity,
        achievement: AchievementEntity,
    ): CouponEntity {
        val couponCode = achievement.generateCouponCode(member.id)
        val expiresAt =
            LocalDateTime.now()
                .plusDays(achievement.couponType?.validityDays ?: 30L)
                .toInstant(ZoneOffset.UTC)

        val coupon =
            CouponEntity(
                code = couponCode,
                member = member,
                achievement = achievement,
                couponType = achievement.couponType!!,
                expiresAt = expiresAt,
            )

        return couponRepository.save(coupon)
    }
}
