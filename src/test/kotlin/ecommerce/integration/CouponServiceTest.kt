package ecommerce.integration

import ecommerce.dto.CouponDTO
import ecommerce.entities.CouponEntity
import ecommerce.entities.MemberEntity
import ecommerce.repositories.CouponRepository
import ecommerce.repositories.MemberRepository
import ecommerce.services.coupon.CouponServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@SpringBootTest
class CouponServiceTest {
    @Autowired
    private lateinit var couponService: CouponServiceImpl

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var member: MemberEntity

    @BeforeEach
    fun setup() {
        member =
            memberRepository.save(
                MemberEntity(
                    name = "a",
                    email = "a@a.com",
                    password = "123",
                    role = MemberEntity.Role.CUSTOMER,
                ),
            )!!
    }

    @Test
    fun `should create a coupon for a valid member and name`() {
        val couponDTO = CouponDTO(name = "FIRST_RANK", memberId = member.id)

        val result = couponService.create(couponDTO)

        assertThat(result.id).isNotNull().isGreaterThan(0L)
        assertThat(result.name).isEqualTo("FIRST_RANK")
        assertThat(result.memberId).isEqualTo(member.id)

        val savedEntity = couponRepository.findById(result.id).get()
        assertThat(savedEntity.discountRate).isEqualTo(CouponEntity.DiscountRate.FIVE_PERCENT)
    }

    @Test
    fun `findAll should return all coupons for a given member`() {
        couponRepository.save(CouponEntity.createFrom("FIRST_RANK", member))
        couponRepository.save(CouponEntity.createFrom("SECOND_RANK", member))

        val coupons = couponService.findAll(member.id)

        assertThat(coupons).hasSize(2)
        assertThat(coupons.map { it.name }).containsExactlyInAnyOrder("FIRST_RANK", "SECOND_RANK")
    }

    @Test
    fun `validateUsability should return true for a non-expired coupon`() {
        val coupon = couponRepository.save(CouponEntity.createFrom("FIRST_RANK", member))

        val isValid = couponService.validateUsability(coupon.id)

        assertThat(isValid).isTrue()
    }

    @Test
    fun `validateUsability should return false for an expired coupon`() {
        val expiredCoupon =
            couponRepository.save(
                CouponEntity(
                    name = "EXPIRED",
                    discountRate = CouponEntity.DiscountRate.FIVE_PERCENT,
                    member = member,
                    expiresAt = LocalDateTime.now().minusDays(1),
                ),
            )

        val isValid = couponService.validateUsability(expiredCoupon.id)
        assertThat(isValid).isFalse()
    }

    @Test
    fun `delete should remove the coupon from the database`() {
        val coupon = couponRepository.save(CouponEntity.createFrom("FIRST_RANK", member))
        assertThat(couponRepository.findById(coupon.id)).isPresent

        couponService.delete(coupon.id)

        assertThat(couponRepository.findById(coupon.id)).isNotPresent
    }
}
