package bass.mappers

import bass.dto.member.MemberLoginDTO
import bass.dto.member.MemberRegisterDTO
import bass.entities.MemberEntity
import bass.entities.TagEntity
import bass.model.Member

fun MemberEntity.toDTO() = Member(name, email, password, role, id)

fun Member.toLoginDTO() = MemberLoginDTO(id)

fun Member.toEntity() = MemberEntity(name, email, password, role, id = id)

fun MemberRegisterDTO.toEntity(tags: Set<TagEntity>): MemberEntity {
    return MemberEntity(
        name = this.name,
        email = this.email,
        password = this.password,
        tags = tags.toMutableSet(),
    )
}
