package bass.controller.order.usecase

import bass.dto.member.MemberLoginDTO
import bass.dto.OrderDTO
import bass.model.PaymentRequest

interface OrderCreationUseCase {
    fun create(
        memberLoginDTO: MemberLoginDTO,
        paymentRequest: PaymentRequest,
    ): OrderDTO
}
