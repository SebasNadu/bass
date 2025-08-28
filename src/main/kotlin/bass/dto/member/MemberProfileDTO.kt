package bass.dto.member

import bass.dto.achievement.AchievementDTO
import bass.dto.coupon.CouponDTO

data class MemberProfileDTO(
    var name: String,
    var email: String,
    var streak: Int,
    var achievements: List<AchievementDTO>,
    val testimonial: String,
    var coupons: List<CouponDTO>,
)
