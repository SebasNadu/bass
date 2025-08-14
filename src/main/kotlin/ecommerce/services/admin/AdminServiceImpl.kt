package ecommerce.services.admin

import ecommerce.controller.admin.usecase.FindMembersWithRecentCartActivityUseCase
import ecommerce.controller.admin.usecase.FindTopProductsUseCase
import ecommerce.dto.ActiveMemberDTO
import ecommerce.dto.TopProductDTO
import ecommerce.repositories.CartItemRepository
import ecommerce.repositories.MealRepository
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
