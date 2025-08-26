package bass.dto.member

import bass.dto.coupon.CouponOrderDTO

class MemberCouponDTO(
    val memberId: Long,
    val newCoupon: CouponOrderDTO? = null,
)
