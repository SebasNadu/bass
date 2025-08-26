package bass.dto.member

import bass.util.ValidationMessages
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class MemberRegisterDTO(
    @field:NotBlank(message = ValidationMessages.MEMBER_NAME_REQUIRED)
    @field:Size(min = 1, max = 50, message = ValidationMessages.MEMBER_NAME_SIZE)
    var name: String,
    @field:NotBlank(message = ValidationMessages.EMAIL_BLANK)
    @field:Email(message = ValidationMessages.EMAIL_INVALID)
    @field:Size(min = 1, max = 100, message = ValidationMessages.EMAIL_SIZE)
    var email: String,
    @field:NotBlank(message = ValidationMessages.PASSWORD_BLANK)
    @field:Size(min = 1, max = 255, message = ValidationMessages.PASSWORD_SIZE)
    var password: String,
    @field:NotEmpty(message = ValidationMessages.TAG_REQUIRED)
    var tagIds: Set<Long>,
    @field:NotEmpty(message = ValidationMessages.DAY_REQUIRED)
    var days: Set<String>,
)
