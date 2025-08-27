package bass.dto.order

import bass.dto.member.MemberCouponDTO

data class OrderCreationResponseDTO(
    val order: OrderDTO,
    val memberDetails: MemberCouponDTO,
)
