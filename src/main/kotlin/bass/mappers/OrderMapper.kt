package bass.mappers

import bass.dto.order.OrderDTO
import bass.dto.order.OrderItemDTO
import bass.dto.order.OrderResponseDTO
import bass.dto.payment.PaymentDTO
import bass.entities.OrderEntity
import bass.entities.OrderItemEntity

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

fun toOrderResponseDTO(order: OrderEntity): OrderResponseDTO {
    return OrderResponseDTO(
        orderId = order.id!!,
        createdAt = order.createdAt.toString(),
        status = order.status.name,
        totalAmount = order.totalAmount,
        items = order.items.map { toOrderItemDTO(it) },
    )
}

fun toOrderItemDTO(orderItem: OrderItemEntity): OrderItemDTO {
    val meal = orderItem.meal
    return OrderItemDTO(
        id = orderItem.id,
        mealId = meal.id,
        mealName = meal.name,
        imageUrl = meal.imageUrl,
        quantity = orderItem.quantity,
        price = orderItem.price,
    )
}
