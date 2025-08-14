package ecommerce.integration

import ecommerce.controller.admin.usecase.FindMembersWithRecentCartActivityUseCase
import ecommerce.controller.admin.usecase.FindTopProductsUseCase
import ecommerce.dto.CartItemRequestDTO
import ecommerce.entities.MealEntity
import ecommerce.entities.MemberEntity
import ecommerce.repositories.CartItemRepository
import ecommerce.repositories.MealRepository
import ecommerce.repositories.MemberRepository
import ecommerce.services.cart.CartItemServiceImpl
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class AdminServiceTest {
    @Autowired
    private lateinit var findTopProductsUseCase: FindTopProductsUseCase

    @Autowired
    private lateinit var findMembersWithRecentCartActivityUseCase: FindMembersWithRecentCartActivityUseCase

    @Autowired
    private lateinit var cartItemService: CartItemServiceImpl

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var mealRepository: MealRepository

    @Autowired
    private lateinit var cartItemRepository: CartItemRepository

    private lateinit var member1: MemberEntity
    private lateinit var member2: MemberEntity

    private lateinit var meal1: MealEntity
    private lateinit var meal2: MealEntity

    @BeforeEach
    fun setup() {
        cartItemRepository.deleteAll()
        mealRepository.deleteAll()
        mealRepository.deleteAll()
        memberRepository.deleteAll()

        member1 = memberRepository.save(MemberEntity(name = "m1", email = "m1@example.com", password = "pw"))!!
        member2 = memberRepository.save(MemberEntity(name = "m2", email = "m2@example.com", password = "pw"))!!

        meal1 =
            mealRepository.save(
                MealEntity(
                    name = "Mouse",
                    imageUrl = "https://lalala.com",
                    quantity = 3,
                    price = 100.0,
                ),
            )

        meal2 =
            mealRepository.save(
                MealEntity(
                    name = "Keyboard",
                    imageUrl = "https://lalala.com",
                    quantity = 3,
                    price = 100.0,
                ),
            )

        cartItemService.addOrUpdate(CartItemRequestDTO(meal1.id, 1), member1.id)
        cartItemService.addOrUpdate(CartItemRequestDTO(meal2.id, 2), member1.id)
        cartItemService.addOrUpdate(CartItemRequestDTO(meal2.id, 1), member2.id)
    }

    @Test
    fun `findTopProductsAddedInList30Days returns top products`() {
        val result = findTopProductsUseCase.findProducts()

        assertThat(result).isNotEmpty
        assertThat(result.map { it.name }).contains("Mouse", "Keyboard")
    }

    @Test
    fun `findMembersWithRecentCartActivity returns distinct members`() {
        val result = findMembersWithRecentCartActivityUseCase.findMembers()

        assertThat(result.map { it.email }).containsExactlyInAnyOrder("m1@example.com", "m2@example.com")
    }
}
