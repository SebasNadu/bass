package ecommerce.dto

import ecommerce.util.ValidationMessages
import jakarta.validation.constraints.Positive

data class CartItemRequestDTO(
    @field:Positive(message = ValidationMessages.ID_REQUIRED)
    val mealId: Long,
    @field:Positive(message = ValidationMessages.QUANTITY_NON_NEGATIVE)
    val quantity: Int,
)
