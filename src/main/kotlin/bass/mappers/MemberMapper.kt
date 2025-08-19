package bass.mappers

import bass.dto.member.MemberLoginDTO
import bass.dto.member.MemberRegisterDTO
import bass.entities.MemberEntity
import bass.model.Member

fun MemberEntity.toDTO() = Member(name, email, password, role, id)

fun Member.toLoginDTO() = MemberLoginDTO(id)

fun Member.toEntity() = MemberEntity(name, email, password, role, id = id)

fun MemberRegisterDTO.toEntity() = MemberEntity(name, email, password)
