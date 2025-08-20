package bass.services.achievement

import bass.entities.CouponEntity

interface AchievementService {
    fun awardAchievement(
        memberId: Long,
        achievementId: Long,
    ): CouponEntity?
}
