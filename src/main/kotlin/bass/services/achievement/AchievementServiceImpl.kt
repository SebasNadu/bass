package bass.services.achievement

import bass.dto.achievement.AchievementDTO
import bass.entities.AchievementEntity
import bass.entities.CouponEntity
import bass.entities.MemberEntity
import bass.repositories.AchievementRepository
import bass.repositories.CouponRepository
import bass.repositories.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

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
            Instant.now()
                .plus(achievement.couponType?.validityDays ?: 30L, ChronoUnit.DAYS)

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

    fun findAllByMemberId(memberId: Long): List<AchievementDTO> {
//        val achievements = achievementRepository.findByMemberId(memberId)
        val achievements = achievementRepository.findAllByMemberId(memberId)
        return achievements.map { AchievementDTO.fromEntity(it) }
    }
}
