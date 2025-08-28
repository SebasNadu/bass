package bass.dto.member

import bass.dto.achievement.AchievementDTO
import bass.dto.coupon.CouponDTO
import bass.dto.day.DayDTO
import bass.dto.tag.TagDTO

data class MemberProfileDTO(
    var name: String,
    var email: String,
    var streak: Int,
    var achievements: List<AchievementDTO>,
    val testimonial: String,
    var coupons: List<CouponDTO>,
    var tags: List<TagDTO>,
    var days: List<DayDTO>
)
