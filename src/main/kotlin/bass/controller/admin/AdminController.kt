package bass.controller.admin

import bass.annotation.CheckAdminOnly
import bass.annotation.IgnoreCheckLogin
import bass.controller.admin.usecase.FindMembersWithRecentCartActivityUseCase
import bass.controller.admin.usecase.FindTopProductsUseCase
import bass.dto.ActiveMemberDTO
import bass.dto.TopProductDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@CheckAdminOnly
class AdminController(
    private val findTopProductsUseCase: FindTopProductsUseCase,
    private val findMembersWithRecentCartActivityUseCase: FindMembersWithRecentCartActivityUseCase,
) {
    @GetMapping("/top-meals")
    fun getTopProducts(): List<TopProductDTO> = findTopProductsUseCase.findProducts()

    @GetMapping("/active-members")
    fun getActiveMembers(): List<ActiveMemberDTO> = findMembersWithRecentCartActivityUseCase.findMembers()

    @IgnoreCheckLogin
    @GetMapping("/slow")
    fun slow(): String {
        Thread.sleep(15_000) // simulate 15s delay
        return "Done"
    }
}
