package bass.controller.member

import bass.controller.member.usecase.AuthUseCase
import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.member.MemberRegisterDTO
import bass.dto.token.TokenRequestDTO
import bass.dto.token.TokenResponseDTO
import bass.infrastructure.AuthorizationExtractor
import bass.model.Member
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val authService: AuthUseCase,
    private val authorizationExtractor: AuthorizationExtractor,
    private val crudMemberUseCase: CrudMemberUseCase,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody memberRegisterDTO: MemberRegisterDTO,
    ): ResponseEntity<TokenResponseDTO> {
        val memberDTO: Member = crudMemberUseCase.save(memberRegisterDTO)
        val tokenResponse = authService.createToken(memberDTO)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody tokenRequestDTO: TokenRequestDTO,
    ): ResponseEntity<TokenResponseDTO> {
        val tokenResponse = authService.login(tokenRequestDTO)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @GetMapping("/me/token")
    fun findMyInfo(request: HttpServletRequest): ResponseEntity<Member> {
        val token = authorizationExtractor.extractToken(request)
        val member = authService.findMemberByToken(token)
        return ResponseEntity.ok().body(member)
    }
}
