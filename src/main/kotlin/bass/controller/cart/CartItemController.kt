package bass.controller.cart

import bass.annotation.LoginMember
import bass.controller.cart.usecase.ManageCartItemUseCase
import bass.dto.CartItemRequestDTO
import bass.dto.CartItemResponseDTO
import bass.dto.MemberLoginDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cart")
class CartItemController(private val cartItemUseCase: ManageCartItemUseCase) {
    @GetMapping
    fun getCartItemsWithProducts(
        @LoginMember member: MemberLoginDTO,
    ): ResponseEntity<List<CartItemResponseDTO>> {
        val cartItems = cartItemUseCase.findByMember(member.id)
        return ResponseEntity.ok().body(cartItems)
    }

    @PostMapping
    fun addOrUpdateCartItem(
        @RequestBody cartItemRequestDTO: CartItemRequestDTO,
        @LoginMember member: MemberLoginDTO,
    ): ResponseEntity<CartItemResponseDTO> {
        val cartItemResponseDTO = cartItemUseCase.addOrUpdate(cartItemRequestDTO, member.id)
        return ResponseEntity.ok().body(cartItemResponseDTO)
    }

    @DeleteMapping
    fun deleteCartItem(
        @RequestBody cartItemRequestDTO: CartItemRequestDTO,
        @LoginMember member: MemberLoginDTO,
    ): ResponseEntity<Unit> {
        cartItemUseCase.delete(cartItemRequestDTO, member.id)
        return ResponseEntity.noContent().build()
    }
}
