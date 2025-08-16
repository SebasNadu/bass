package ecommerce.controller.coupon.usecase

import ecommerce.dto.CouponDTO

interface ManageCouponUseCase {
    fun create(couponDTO: CouponDTO): CouponDTO

    fun findAll(memberId: Long): List<CouponDTO>

    fun validateUsability(couponId: Long): Boolean

    fun delete(couponId: Long)
}
