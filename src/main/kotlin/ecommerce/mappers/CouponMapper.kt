package ecommerce.mappers

import ecommerce.dto.CouponDTO
import ecommerce.entities.CouponEntity

fun CouponEntity.toDTO() = CouponDTO(name, discountRate.toString(), expiresAt, id)
