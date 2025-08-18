package bass.controller.cart.usecase

import bass.dto.CartItemRequestDTO
import bass.dto.CartItemResponseDTO

interface ManageCartItemUseCase {
    fun addOrUpdate(
        cartItemRequestDTO: CartItemRequestDTO,
        memberId: Long,
    ): CartItemResponseDTO

    fun findByMember(memberId: Long): List<CartItemResponseDTO>

    fun delete(
        cartItemRequestDTO: CartItemRequestDTO,
        memberId: Long,
    )

    fun deleteAll()
}
