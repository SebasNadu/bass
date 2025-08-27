package bass.mappers

import bass.dto.order.OrderDTO
import bass.dto.payment.PaymentDTO
import bass.entities.OrderEntity

fun OrderEntity.toDTO(paymentsWithDiscount: List<PaymentDTO>): OrderDTO =
    OrderDTO(
        status = status,
        totalAmount = totalAmount,
        memberId = member.id,
        items = items.map { it.toDTO() },
        payments = paymentsWithDiscount,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString(),
        id = id,
    )
