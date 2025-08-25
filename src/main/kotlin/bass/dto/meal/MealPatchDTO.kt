package bass.dto.meal

import bass.util.ValidationMessages
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class MealPatchDTO(
    @field:Size(min = 1, max = 50, message = ValidationMessages.PRODUCT_NAME_SIZE)
    @field:Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_]*$", message = ValidationMessages.NAME_PATTERN)
    var name: String? = null,
    @field:Positive(message = ValidationMessages.PRICE_POSITIVE)
    var price: BigDecimal? = null,
    @field:Pattern(regexp = "^https?://.*$", message = ValidationMessages.IMAGE_FORMAT)
    var imageUrl: String? = null,
    @field:Positive(message = ValidationMessages.QUANTITY_NON_NEGATIVE)
    var quantity: Int? = null,
    var description: String? = null,
    var tagsIds: Set<Long> = emptySet(),
)
