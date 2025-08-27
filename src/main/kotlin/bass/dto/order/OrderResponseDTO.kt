package bass.dto.order

import java.math.BigDecimal

class OrderResponseDTO(
    val orderId: Long,
    val createdAt: String,
    val status: String,
    val totalAmount: BigDecimal,
    val items: List<OrderItemDTO> = mutableListOf(),
)
