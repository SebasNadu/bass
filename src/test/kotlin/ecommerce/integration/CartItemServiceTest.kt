package ecommerce.integration

import ecommerce.controller.cart.usecase.ManageCartItemUseCase
import ecommerce.dto.CartItemRequestDTO
import ecommerce.entities.MealEntity
import ecommerce.mappers.toDTO
import ecommerce.repositories.MealRepository
import ecommerce.repositories.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class CartItemServiceTest {
    @Autowired
    private lateinit var cartItemService: ManageCartItemUseCase

    @Autowired
    private lateinit var mealRepository: MealRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private var mealId: Long = 0
    private val memberId = 1L

    @BeforeEach
    fun setup() {
        mealId =
            mealRepository.save(
                MealEntity(
                    name = "Mechanical Keyboard",
                    quantity = 2,
                    imageUrl = "https://lalal.com",
                    price = 100.0,
                ),
            ).id
    }

    @Test
    fun `addOrUpdate should create new cart item if not exists`() {
        val dto = CartItemRequestDTO(mealId = mealId, quantity = 2)
        val member = memberRepository.findByIdOrNull(memberId)?.toDTO()

        val response = cartItemService.addOrUpdate(dto, member?.id!!)

        assertThat(response.id).isNotNull()
        assertThat(response.quantity).isEqualTo(2)
        assertThat(response.meal.name).isEqualTo("Mechanical Keyboard")
    }

    @Test
    fun `addOrUpdate should update quantity if item exists`() {
        val initial = CartItemRequestDTO(mealId = mealId, quantity = 1)
        val member = memberRepository.findByIdOrNull(memberId)?.toDTO()
        cartItemService.addOrUpdate(initial, member?.id!!)

        val updated = CartItemRequestDTO(mealId = mealId, quantity = 5)
        val result = cartItemService.addOrUpdate(updated, member.id)

        assertThat(result.quantity).isEqualTo(5)
    }

    @Test
    fun `addOrUpdate should throw if product not found`() {
        val badDto = CartItemRequestDTO(mealId = 9999L, quantity = 1)
        val member = memberRepository.findByIdOrNull(memberId)?.toDTO()

        assertThrows<EmptyResultDataAccessException> {
            cartItemService.addOrUpdate(badDto, member?.id!!)
        }
    }

    @Test
    fun `findByMember should return cart items for a member`() {
        val member = memberRepository.findByIdOrNull(memberId)?.toDTO()
        cartItemService.addOrUpdate(CartItemRequestDTO(mealId, 1), member?.id!!)

        val items = cartItemService.findByMember(memberId)

        assertThat(items).hasSize(1)
        assertThat(items[0].meal.id).isEqualTo(mealId)
        assertThat(items[0].quantity).isEqualTo(1)
    }

    @Test
    fun `delete should remove cart item for member`() {
        val member = memberRepository.findByIdOrNull(memberId)?.toDTO()
        cartItemService.addOrUpdate(CartItemRequestDTO(mealId, 2), member?.id!!)

        cartItemService.delete(CartItemRequestDTO(mealId, 2), memberId)

        val items = cartItemService.findByMember(memberId)
        assertThat(items).isEmpty()
    }
}
