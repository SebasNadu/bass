package bass.services.member

import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.member.MemberRegisterDTO
import bass.entities.DayEntity
import bass.entities.MemberEntity
import bass.exception.DaysSizeAlreadyMaximumException
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.model.Member
import bass.repositories.MemberRepository
import bass.repositories.TagRepository
import bass.util.logger
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val tagRepository: TagRepository,
) : CrudMemberUseCase {
    private val log = logger<MemberServiceImpl>()

    @Transactional(readOnly = true)
    override fun findAll(): List<Member> {
        val members = memberRepository.findAll().map { it.toDTO() }
        log.info("Found ${members.size} members")
        return members
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Member {
        val member =
            memberRepository.findByIdOrNull(id)?.toDTO()
                ?: run {
                    log.warn("Member with id=$id not found")
                    throw EmptyResultDataAccessException("Member with ID $id not found", 1)
                }
        log.info("Found member: $member by their id: $id ")
        return member
    }

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): Member {
        val member =
            memberRepository.findByEmail(email)?.toDTO()
                ?: run {
                    log.warn("Member with email=$email not found")
                    throw EmptyResultDataAccessException("Member with Email $email not found", 1)
                }
        log.info("Found member: ${member.name} by their email: $email ")
        return member
    }

    @Transactional
    override fun save(memberRegisterDTO: MemberRegisterDTO): Member {
        validateEmailUniqueness(memberRegisterDTO.email)
        val selectedTags = tagRepository.findAllById(memberRegisterDTO.tagIds).toMutableSet()

        val days =
            memberRegisterDTO.days.map { day ->
                DayEntity.DayOfWeek.valueOf(day.uppercase())
            }

        if (days.size !in MemberEntity.DAYS_SIZE_MIN..MemberEntity.DAYS_SIZE_MAX) {
            throw DaysSizeAlreadyMaximumException(
                "DaysSizeAlreadyMaximumException: $days can't be bigger than ${memberRegisterDTO.days.size}",
            )
        }

        val member = memberRegisterDTO.toEntity(tags = selectedTags, days = mutableSetOf())

        val dayEntities = days.map { DayEntity(it) }.toMutableSet()
        dayEntities.forEach { it.setMemberEntity(member) }

        member.days.addAll(dayEntities)

        val saved =
            memberRepository.save(member)
                ?: run {
                    log.error("Failed to save ${memberRegisterDTO.email} to $memberRegisterDTO")
                    throw OperationFailedException("Failed to save member")
                }
        log.info("Successfully saved member's email:${memberRegisterDTO.email} as ${saved.email}")
        return saved.toDTO()
    }

    @Transactional
    override fun deleteAll() {
        log.debug("Deleting all Members")
        memberRepository.deleteAll()
        log.info("All Members have been deleted!")
    }

    @Transactional(readOnly = true)
    override fun validateEmailUniqueness(email: String) {
        if (memberRepository.existsByEmail(email)) {
            log.warn("Email: $email already exists!")
            throw OperationFailedException("Member with email '$email' already exists")
        }
        log.info("Email $email is unique")
    }
}
