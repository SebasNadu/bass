package bass.mappers

import bass.dto.coupon.CouponDTO
import bass.dto.coupon.CouponOrderDTO
import bass.entities.CouponEntity

fun CouponEntity.toDTO() =
    CouponDTO(
        code = code,
        displayName = couponType.displayName,
        discountType = discountType,
        discountValue = discountValue,
        discountAmount = discountAmount,
        validityDays = validityDays,
        isValid = isValid(),
        expiresAt = expiresAt,
        id = id,
    )

fun CouponEntity.toOrderDTO(): CouponOrderDTO {
    return CouponOrderDTO(
        displayName = this.achievement.name,
        memberStreak = this.member.streak,
        achievementDescription = this.achievement.description,
    )
}
