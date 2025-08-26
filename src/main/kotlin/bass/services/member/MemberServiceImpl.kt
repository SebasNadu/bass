package bass.services.member

import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.member.MemberRegisterDTO
import bass.entities.DayEntity
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.model.Member
import bass.repositories.DayRepository
import bass.repositories.MemberRepository
import bass.repositories.TagRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val tagRepository: TagRepository,
    private val dayRepository: DayRepository,
) : CrudMemberUseCase {
    @Transactional(readOnly = true)
    override fun findAll(): List<Member> {
        return memberRepository.findAll().map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Member =
        memberRepository.findByIdOrNull(id)?.toDTO()
            ?: throw EmptyResultDataAccessException("Member with ID $id not found", 1)

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): Member {
        return memberRepository.findByEmail(email)?.toDTO()
            ?: throw EmptyResultDataAccessException("Member with Email $email not found", 1)
    }

    @Transactional
    override fun save(memberRegisterDTO: MemberRegisterDTO): Member {
        validateEmailUniqueness(memberRegisterDTO.email)
        val selectedTags = tagRepository.findAllById(memberRegisterDTO.tagIds).toMutableSet()
        val freedomDays = extractFreedomDaysFromDTO(memberRegisterDTO)
        val saved =
            memberRepository.save(memberRegisterDTO.toEntity(selectedTags))
                ?: throw OperationFailedException("Failed to save product")
        freedomDays.forEach { saved.addDay(it) }
        return saved.toDTO()
    }

    @Transactional
    override fun deleteAll() {
        memberRepository.deleteAll()
    }

    @Transactional(readOnly = true)
    override fun validateEmailUniqueness(email: String) {
        if (memberRepository.existsByEmail(email)) {
            throw OperationFailedException("Member with email '$email' already exists")
        }
    }

    private fun extractFreedomDaysFromDTO(memberRegisterDTO: MemberRegisterDTO): Set<DayEntity> {
        val freedomDays = memberRegisterDTO.freedomDays.map { dayName ->
            DayEntity(DayEntity.DayOfWeek.valueOf(dayName))
        }.toSet()
        return freedomDays
    }
}
