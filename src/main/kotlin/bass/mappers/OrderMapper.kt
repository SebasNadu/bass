package bass.mappers

import bass.dto.OrderDTO
import bass.entities.OrderEntity

fun OrderEntity.toDTO(): OrderDTO =
    OrderDTO(
        status = status,
        totalAmount = totalAmount,
        memberId = member.id,
        items = items.map { it.toDTO() },
        payments = payments.map { it.toDTO() },
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString(),
        id = id,
    )
