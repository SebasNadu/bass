package bass.controller.member

import bass.annotation.LoginMember
import bass.controller.member.usecase.AuthUseCase
import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.TokenRequestDTO
import bass.dto.TokenResponseDTO
import bass.dto.member.MemberLoginDTO
import bass.dto.member.MemberProfileDTO
import bass.dto.member.MemberRegisterDTO
import bass.infrastructure.AuthorizationExtractor
import bass.mappers.toProfileDTO
import bass.model.Member
import bass.services.achievement.AchievementServiceImpl
import bass.services.coupon.CouponServiceImpl
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val authService: AuthUseCase,
    private val authorizationExtractor: AuthorizationExtractor,
    private val crudMemberUseCase: CrudMemberUseCase,
    private val achievementService: AchievementServiceImpl,
    private val couponServiceImpl: CouponServiceImpl,
) {
    @PostMapping(MEMBERS_PATH_REGISTER)
    fun register(
        @Valid @RequestBody memberRegisterDTO: MemberRegisterDTO,
    ): ResponseEntity<TokenResponseDTO> {
        val memberDTO: Member = crudMemberUseCase.save(memberRegisterDTO)
        val tokenResponse = authService.createToken(memberDTO)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @PostMapping(MEMBERS_PATH_LOGIN)
    fun login(
        @Valid @RequestBody tokenRequestDTO: TokenRequestDTO,
    ): ResponseEntity<TokenResponseDTO> {
        val tokenResponse = authService.login(tokenRequestDTO)
        return ResponseEntity.ok().body(tokenResponse)
    }

    @GetMapping(MEMBERS_PATH_TOKEN)
    fun findMyInfo(request: HttpServletRequest): ResponseEntity<Member> {
        val token = authorizationExtractor.extractToken(request)
        val member = authService.findMemberByToken(token)
        return ResponseEntity.ok().body(member)
    }

    @GetMapping(MEMBERS_PATH_ME)
    fun findMyProfile(
        @LoginMember memberLoginDTO: MemberLoginDTO,
    ): ResponseEntity<MemberProfileDTO> {
        val member = crudMemberUseCase.findById(memberLoginDTO.id)
        val achievements = achievementService.findAllByMemberId(member.id)
        val coupons = couponServiceImpl.findAll(member.id)
        return ResponseEntity.ok().body(
            member.toProfileDTO(
                achievements = achievements,
                coupons = coupons,
            ),
        )
    }

    companion object {
        const val MEMBERS_PATH = "/api/members"
        const val MEMBERS_PATH_ME = "$MEMBERS_PATH/me"
        const val MEMBERS_PATH_REGISTER = "$MEMBERS_PATH/register"
        const val MEMBERS_PATH_LOGIN = "$MEMBERS_PATH/login"
        const val MEMBERS_PATH_TOKEN = "$MEMBERS_PATH/me/token"
    }
}
