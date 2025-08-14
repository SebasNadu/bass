package ecommerce.dto

class OrderItemDTO(
    val mealId: Long,
    val quantity: Int,
    val price: Double,
    var id: Long? = null,
)
