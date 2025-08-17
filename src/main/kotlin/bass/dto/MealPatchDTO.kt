package bass.dto

import bass.util.ValidationMessages
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class MealPatchDTO(
    @field:Size(min = 1, max = 15, message = ValidationMessages.PRODUCT_NAME_SIZE)
    @field:Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$", message = ValidationMessages.NAME_PATTERN)
    var name: String? = null,
    @field:Positive(message = ValidationMessages.PRICE_POSITIVE)
    var price: Double? = null,
    @field:Pattern(regexp = "^https?://.*$", message = ValidationMessages.IMAGE_FORMAT)
    var imageUrl: String? = null,
    @field:Positive(message = ValidationMessages.QUANTITY_NON_NEGATIVE)
    var quantity: Int? = null,
    val options: Set<MealDTO> = emptySet(),
)
