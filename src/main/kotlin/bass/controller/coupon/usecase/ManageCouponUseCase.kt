package bass.controller.coupon.usecase

import bass.dto.coupon.CouponDTO

interface ManageCouponUseCase {
    fun findAll(memberId: Long): List<CouponDTO>

    fun validateUsability(couponId: Long): Boolean

    fun delete(couponId: Long)
}
