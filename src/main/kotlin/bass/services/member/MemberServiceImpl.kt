package bass.services.member

import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.member.MemberRegisterDTO
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.model.Member
import bass.repositories.MemberRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberServiceImpl(private val memberRepository: MemberRepository) : CrudMemberUseCase {
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
        val saved =
            memberRepository.save(memberRegisterDTO.toEntity())
                ?: throw OperationFailedException("Failed to save product")
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
}
