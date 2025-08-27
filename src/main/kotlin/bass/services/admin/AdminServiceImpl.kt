package bass.services.admin

import bass.controller.admin.usecase.FindMembersWithRecentCartActivityUseCase
import bass.controller.admin.usecase.FindTopProductsUseCase
import bass.dto.member.ActiveMemberDTO
import bass.dto.product.TopProductDTO
import bass.repositories.CartItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminServiceImpl(
    private val cartItemRepository: CartItemRepository,
) : FindTopProductsUseCase, FindMembersWithRecentCartActivityUseCase {
    @Transactional(readOnly = true)
    override fun findProducts(): List<TopProductDTO> {
        return cartItemRepository.findTop5ProductsAddedInLast30Days()
    }

    @Transactional(readOnly = true)
    override fun findMembers(): List<ActiveMemberDTO> {
        return cartItemRepository.findDistinctMembersWithCartActivityInLast7Days()
    }
}
