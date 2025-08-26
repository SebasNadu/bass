package bass.mappers

import bass.dto.member.MemberLoginDTO
import bass.dto.member.MemberRegisterDTO
import bass.entities.DayEntity
import bass.entities.MemberEntity
import bass.entities.TagEntity
import bass.model.Member

fun MemberEntity.toDTO(): Member {
    return Member(
        name = this.name,
        email = this.email,
        password = this.password,
        role = this.role,
        id = this.id,
        tags = this.tags.map { it.toDTO() }.toSet(),
        days = this.days.map { it.toDTO() }.toSet(),
    )
}

fun Member.toLoginDTO() = MemberLoginDTO(id)

fun Member.toEntity() = MemberEntity(name, email, password, role, id = id)

fun MemberRegisterDTO.toEntity(
    tags: Set<TagEntity>,
    days: Set<DayEntity>,
): MemberEntity {
    return MemberEntity(
        name = this.name,
        email = this.email,
        password = this.password,
        tags = tags.toMutableSet(),
        days = days.toMutableSet(),
    )
}
