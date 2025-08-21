package bass.dto

import java.math.BigDecimal

class OrderItemDTO(
    val mealId: Long,
    val quantity: Int,
    val price: BigDecimal,
    var id: Long? = null,
)
