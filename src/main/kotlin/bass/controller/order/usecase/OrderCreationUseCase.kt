package bass.controller.order.usecase

import bass.dto.order.OrderDTO
import bass.dto.member.MemberLoginDTO
import bass.model.PaymentRequest

interface OrderCreationUseCase {
    fun create(
        memberLoginDTO: MemberLoginDTO,
        paymentRequest: PaymentRequest,
    ): OrderDTO
}
