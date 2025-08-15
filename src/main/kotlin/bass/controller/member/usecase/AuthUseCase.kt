package bass.controller.member.usecase

import bass.dto.TokenRequestDTO
import bass.dto.TokenResponseDTO
import bass.model.Member

interface AuthUseCase {
    fun login(tokenRequestDTO: TokenRequestDTO): TokenResponseDTO

    fun checkInvalidLogin(tokenRequestDTO: TokenRequestDTO): Boolean

    fun findMemberByToken(token: String): Member

    fun createToken(memberDTO: Member): TokenResponseDTO
}
