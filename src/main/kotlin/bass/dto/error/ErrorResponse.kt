package bass.dto.error

import java.time.Instant

data class ErrorResponse(
    val status: Int,
    val errorLabel: String,
    val message: Any,
    val timestamp: Instant,
    val errors: List<ErrorMessage>? = null,
)
