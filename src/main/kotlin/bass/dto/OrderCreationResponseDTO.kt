package bass.dto

import bass.dto.member.MemberCouponDTO

data class OrderCreationResponseDTO(
    val order: OrderDTO,
    val memberDetails: MemberCouponDTO,
)
