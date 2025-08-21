package bass.dto

import bass.entities.OrderEntity
import java.math.BigDecimal

class OrderDTO(
    val status: OrderEntity.OrderStatus,
    val totalAmount: BigDecimal,
    val memberId: Long,
    val items: List<OrderItemDTO> = mutableListOf(),
    val payments: List<PaymentDTO> = mutableListOf(),
    val createdAt: String,
    val updatedAt: String,
    var id: Long? = null,
)
