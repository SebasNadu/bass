package bass.services.cart

import bass.controller.cart.usecase.ManageCartItemUseCase
import bass.controller.member.usecase.CrudMemberUseCase
import bass.dto.cartItem.CartItemRequestDTO
import bass.dto.cartItem.CartItemResponseDTO
import bass.entities.CartItemEntity
import bass.exception.OperationFailedException
import bass.mappers.toDTO
import bass.mappers.toEntity
import bass.repositories.CartItemRepository
import bass.repositories.MealRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class CartItemServiceImpl(
    private val cartItemRepository: CartItemRepository,
    private val mealRepository: MealRepository,
    private val memberService: CrudMemberUseCase,
) : ManageCartItemUseCase {
    @Transactional
    override fun addOrUpdate(
        cartItemRequestDTO: CartItemRequestDTO,
        memberId: Long,
    ): CartItemResponseDTO {
        validateMealExists(cartItemRequestDTO.mealId)

        val cartItem =
            if (!cartItemRepository.existsByMealIdAndMemberId(cartItemRequestDTO.mealId, memberId)) {
                handleCreate(cartItemRequestDTO, memberId)
            } else {
                handleUpdate(cartItemRequestDTO, memberId)
            }

        return cartItem.toDTO()
    }

    @Transactional(readOnly = true)
    override fun findByMember(memberId: Long): List<CartItemResponseDTO> {
        val itemsWithProducts = cartItemRepository.findByMemberId(memberId)

        return itemsWithProducts.map { cartItem ->
            CartItemResponseDTO(
                id = cartItem.id,
                memberId = cartItem.member.id,
                meal = cartItem.meal.toDTO(),
                quantity = cartItem.quantity,
                addedAt = cartItem.addedAt,
            )
        }
    }

    @Transactional
    override fun delete(
        cartItemRequestDTO: CartItemRequestDTO,
        memberId: Long,
    ) {
        cartItemRepository.deleteByMealIdAndMemberId(cartItemRequestDTO.mealId, memberId)
    }

    private fun validateMealExists(mealId: Long) {
        if (!mealRepository.existsById(mealId)) {
            throw EmptyResultDataAccessException("Product with ID $mealId does not exist", 1)
        }
    }

    @Transactional
    override fun deleteAll() {
        cartItemRepository.deleteAll()
    }

    private fun handleCreate(
        cartItemRequestDTO: CartItemRequestDTO,
        memberId: Long,
    ): CartItemEntity {
        val option =
            mealRepository.findByIdOrNull(cartItemRequestDTO.mealId)
                ?: throw OperationFailedException("Invalid Product Id ${cartItemRequestDTO.mealId}")
        option.checkStock(cartItemRequestDTO.quantity)
        val member = memberService.findById(memberId)
        return cartItemRepository.save(
            CartItemEntity(
                member = member.toEntity(),
                meal = option,
                quantity = cartItemRequestDTO.quantity,
                addedAt = Instant.now(),
            ),
        )
    }

    private fun handleUpdate(
        cartItemRequestDTO: CartItemRequestDTO,
        memberId: Long,
    ): CartItemEntity {
        val existing =
            cartItemRepository
                .findByMealIdAndMemberId(cartItemRequestDTO.mealId, memberId)
                ?: throw OperationFailedException("Cart item not found")

        if (cartItemRequestDTO.quantity <= 0) throw OperationFailedException("Quantity must be greater than zero")
        if (existing.quantity == cartItemRequestDTO.quantity) return existing

        existing.quantity = cartItemRequestDTO.quantity
        return cartItemRepository.save(existing)
    }
}
