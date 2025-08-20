package bass.controller.coupon.usecase

import bass.dto.CouponDTO

interface ManageCouponUseCase {
//    fun create(couponDTO: CouponDTO): CouponDTO

    fun findAll(memberId: Long): List<CouponDTO>

    fun validateUsability(couponId: Long): Boolean

    fun delete(couponId: Long)
}
