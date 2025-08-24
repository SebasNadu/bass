package bass.integration

import bass.dto.member.MemberLoginDTO
import bass.entities.AchievementEntity
import bass.entities.CartItemEntity
import bass.entities.MealEntity
import bass.entities.MemberEntity
import bass.enums.CouponType
import bass.model.PaymentRequest
import bass.repositories.AchievementRepository
import bass.repositories.CartItemRepository
import bass.repositories.CouponRepository
import bass.repositories.MealRepository
import bass.repositories.MemberRepository
import bass.services.order.OrderServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class StreakServiceTest {
    @Autowired
    lateinit var orderService: OrderServiceImpl

    @Autowired
    private lateinit var achievementRepository: AchievementRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var cartItemRepository: CartItemRepository

    @Autowired
    lateinit var couponRepository: CouponRepository

    @Autowired
    lateinit var mealRepository: MealRepository

    lateinit var member: MemberEntity
    lateinit var meal: MealEntity
    lateinit var cartItem: CartItemEntity
    private lateinit var couponAchievement: AchievementEntity

    @BeforeEach
    fun setup() {
        val members = memberRepository.findAll()
        member = members[0]

        val meals = mealRepository.findAll()
        meal = meals[0]

        val cartItemEntity =
            CartItemEntity(
                member = member,
                meal = meal,
                quantity = 2,
                addedAt = LocalDateTime.now(),
            )

        cartItem = cartItemRepository.save(cartItemEntity)
    }

    @Test
    fun `should give coupon to member after order payment`() {
        couponAchievement =
            achievementRepository.save(
                AchievementEntity(
                    name = "Coupon Achievement",
                    streaksRequired = 1,
                    couponType = CouponType.FIRST_RANK,
                    description = "Generates a coupon",
                ),
            )
        val memberLoginDTO = MemberLoginDTO(id = member.id)
        val paymentRequest =
            PaymentRequest(
                amount = "29.00".toBigDecimal(),
                currency = "eur",
                paymentMethod = "pm_card_visa",
            )

        orderService.create(memberLoginDTO, paymentRequest)

        val updatedMember = memberRepository.findById(member.id).get()
        assertThat(updatedMember.streak).isEqualTo(1)
        val coupons = couponRepository.findByMemberId(member.id)
        assertThat(coupons[0].achievement.name).isEqualTo("Coupon Achievement")
        assertThat(coupons).hasSize(1)
    }
}
