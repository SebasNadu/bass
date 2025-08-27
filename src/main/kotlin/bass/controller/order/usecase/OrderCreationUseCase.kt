package bass.controller.order.usecase

import bass.dto.member.MemberLoginDTO
import bass.dto.order.OrderDTO
import bass.dto.order.OrderResponseDTO
import bass.model.PaymentRequest
import org.springframework.data.domain.Pageable

interface OrderCreationUseCase {
    fun create(
        memberLoginDTO: MemberLoginDTO,
        paymentRequest: PaymentRequest,
    ): OrderDTO

    fun getAllOrdersByMemberId(
        memberId: Long,
        pageable: Pageable,
    ): List<OrderResponseDTO>
}
