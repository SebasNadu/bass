package bass.services.achievement

import bass.repositories.AchievementRepository
import bass.repositories.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AchievementServiceImpl(
    private val memberRepository: MemberRepository,
    private val achievementRepository: AchievementRepository,
//    private val couponRepository: CouponRepository
) {

//    fun awardAchievement(memberId: Long, achievementId: Long): Coupon? {
//        val member = memberRepository.findById(memberId).orElseThrow()
//        val achievement = achievementRepository.findById(achievementId).orElseThrow()
//
//        // Add achievement to member
//        member.achievements.add(achievement)
//        memberRepository.save(member)
//
//        // Generate coupon if achievement has one
//        return if (achievement.generatesCoupon()) {
//            generateCoupon(member, achievement)
//        } else null
//    }

//    private fun generateCoupon(member: Member, achievement: Achievement): Coupon {
//        val couponCode = achievement.generateCouponCode(member.id) ?:
//        throw IllegalStateException("Cannot generate coupon code")
//
//        val expiresAt = LocalDateTime.now().plusDays(
//            achievement.couponValidityDays?.toLong() ?: 30L
//        )
//
//        val coupon = Coupon(
//            code = couponCode,
//            member = member,
//            achievement = achievement,
//            type = achievement.couponType!!,
//            discountValue = achievement.discountValue ?: BigDecimal.ZERO,
//            expiresAt = expiresAt
//        )
//
//        return couponRepository.save(coupon)
//    }
}
