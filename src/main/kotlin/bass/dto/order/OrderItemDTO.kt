package bass.dto.order

import java.math.BigDecimal

class OrderItemDTO(
    val mealId: Long,
    val quantity: Int,
    val mealName: String,
    val imageUrl: String,
    val price: BigDecimal,
    var id: Long? = null,
)
