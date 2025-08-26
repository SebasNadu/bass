package bass.model

import bass.dto.tag.TagDTO
import bass.entities.MemberEntity
import bass.entities.TagEntity
import bass.util.ValidationMessages.EMAIL_BLANK
import bass.util.ValidationMessages.EMAIL_INVALID
import bass.util.ValidationMessages.EMAIL_SIZE
import bass.util.ValidationMessages.MEMBER_NAME_REQUIRED
import bass.util.ValidationMessages.MEMBER_NAME_SIZE
import bass.util.ValidationMessages.PASSWORD_BLANK
import bass.util.ValidationMessages.PASSWORD_SIZE
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class Member(
    @field:NotBlank(message = MEMBER_NAME_REQUIRED)
    @field:Size(min = 1, max = 50, message = MEMBER_NAME_SIZE)
    var name: String,
    @field:NotBlank(message = EMAIL_BLANK)
    @field:Email(message = EMAIL_INVALID)
    @field:Size(min = 1, max = 100, message = EMAIL_SIZE)
    var email: String,
    @field:NotBlank(message = PASSWORD_BLANK)
    @field:Size(min = 1, max = 255, message = PASSWORD_SIZE)
    var password: String,
    var role: MemberEntity.Role = MemberEntity.Role.CUSTOMER,
    var testimonial: String,
    var freedomDays: Set<String>,
    var tags: Set<TagDTO>,
    var id: Long = 0L,
) {
    fun validatePassword(password: String): Boolean = this.password == password

    fun validateEmail(email: String): Boolean = this.email == email
}
