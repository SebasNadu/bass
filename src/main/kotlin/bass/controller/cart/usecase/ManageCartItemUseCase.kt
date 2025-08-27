package bass.controller.cart.usecase

import bass.dto.cartItem.CartItemRequestDTO
import bass.dto.cartItem.CartItemResponseDTO

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
