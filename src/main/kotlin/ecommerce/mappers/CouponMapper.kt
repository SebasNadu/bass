package ecommerce.mappers

import ecommerce.dto.CouponDTO
import ecommerce.entities.CouponEntity

fun CouponEntity.toDTO() = CouponDTO(name, member.id, discountRate.toString(), expiresAt, id)
