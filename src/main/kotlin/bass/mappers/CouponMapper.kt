package bass.mappers

import bass.dto.CouponDTO
import bass.entities.CouponEntity

fun CouponEntity.toDTO() = CouponDTO(name, member.id, discountRate.toString(), expiresAt, id)
