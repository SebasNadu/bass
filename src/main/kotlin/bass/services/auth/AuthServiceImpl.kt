package bass.services.auth

import bass.controller.member.usecase.AuthUseCase
import bass.dto.token.TokenRequestDTO
import bass.dto.token.TokenResponseDTO
import bass.exception.ForbiddenException
import bass.infrastructure.JwtTokenProvider
import bass.mappers.toDTO
import bass.model.Member
import bass.repositories.MemberRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(private val jwtTokenProvider: JwtTokenProvider, private val memberRepository: MemberRepository) :
    AuthUseCase {
    @Transactional
    override fun login(tokenRequestDTO: TokenRequestDTO): TokenResponseDTO {
        if (checkInvalidLogin(tokenRequestDTO)) throw ForbiddenException("Invalid email or password.")
        val memberDTO = findMemberByEmail(tokenRequestDTO.email)
        return createToken(memberDTO)
    }

    override fun checkInvalidLogin(tokenRequestDTO: TokenRequestDTO): Boolean {
        val memberDTO = findMemberByEmail(tokenRequestDTO.email)
        return !memberDTO.validateEmail(tokenRequestDTO.email) || !memberDTO.validatePassword(tokenRequestDTO.password)
    }

    @Transactional(readOnly = true)
    override fun findMemberByToken(token: String): Member {
        val (email, _) = jwtTokenProvider.getPayload(token)
        return findMemberByEmail(email)
    }

    @Transactional
    override fun createToken(memberDTO: Member): TokenResponseDTO {
        val accessToken = jwtTokenProvider.createToken(memberDTO.email, memberDTO.role)
        return TokenResponseDTO(accessToken)
    }

    private fun findMemberByEmail(email: String): Member {
        return memberRepository.findByEmail(email)?.toDTO() ?: throw EmptyResultDataAccessException(
            "Member with Email $email not found",
            1,
        )
    }
}
