package bass.mappers

import bass.dto.OrderDTO
import bass.dto.PaymentDTO
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
