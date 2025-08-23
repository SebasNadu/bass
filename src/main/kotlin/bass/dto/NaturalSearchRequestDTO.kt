package bass.dto

import jakarta.validation.constraints.NotBlank

data class NaturalSearchRequestDTO(
    @field:NotBlank
    val userText: String,
    val requireAllTags: Boolean = false,
    val maxTags: Int = 8
)
