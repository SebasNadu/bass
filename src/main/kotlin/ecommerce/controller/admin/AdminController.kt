package ecommerce.controller.admin

import ecommerce.annotation.CheckAdminOnly
import ecommerce.annotation.IgnoreCheckLogin
import ecommerce.controller.admin.usecase.FindMembersWithRecentCartActivityUseCase
import ecommerce.controller.admin.usecase.FindTopProductsUseCase
import ecommerce.dto.ActiveMemberDTO
import ecommerce.dto.TopProductDTO
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
    @GetMapping("/top-products")
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
