package bass.dto.error

import bass.entities.Auditable

data class ErrorResponse(
    val status: Int,
    val errorLabel: String,
    val message: Any,
    val errors: List<ErrorMessage>? = null,
) : Auditable()
