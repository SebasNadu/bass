package bass.mappers

import bass.dto.coupon.CouponDTO
import bass.entities.CouponEntity

fun CouponEntity.toDTO() = CouponDTO(
    code = code,
    displayName = couponType.displayName,
    discountType = discountType,
    discountValue = discountValue,
    discountAmount = discountAmount,
    validityDays = validityDays,
    isValid = isValid(),
    expiresAt = expiresAt,
    id = id
)
