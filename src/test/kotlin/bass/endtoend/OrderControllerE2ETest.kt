package bass.endtoend

import bass.dto.order.OrderCreationResponseDTO
import bass.dto.token.TokenRequestDTO
import bass.dto.meal.MealRequestDTO
import bass.dto.meal.MealResponseDTO
import bass.entities.AchievementEntity
import bass.entities.CartItemEntity
import bass.entities.MemberEntity
import bass.enums.CouponType
import bass.model.PaymentRequest
import bass.repositories.AchievementRepository
import bass.repositories.CartItemRepository
import bass.repositories.MealRepository
import bass.repositories.MemberRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal
import java.time.Instant

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OrderControllerE2ETest(
    @param:Autowired
    var cartItemRepository: CartItemRepository,
    @param:Autowired
    var memberRepository: MemberRepository,
    @param:Autowired
    var mealRepository: MealRepository,
    @Autowired
    var achievementRepository: AchievementRepository,
) {
    private lateinit var token: String
    private var memberId: Long = 0L
    private lateinit var member: MemberEntity

    @BeforeEach
    fun loginAndPrepareData() {
        val loginPayload =
            TokenRequestDTO(
                "sebas@sebas.com",
                "123456",
            )
        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .post("/api/members/login")
                .then().extract()

        token = response.body().jsonPath().getString("accessToken")
        Assertions.assertThat(token).isNotBlank

        member = memberRepository.findByEmail(loginPayload.email)!!
        memberId = member.id
    }

    @Test
    fun `create order success`() {
        achievementRepository.save(
            AchievementEntity(
                name = "Coupon Achievement",
                streaksRequired = 1,
                couponType = CouponType.FIRST_RANK,
                description = "Generates a coupon",
            ),
        )

        createCartItem()
        val paymentRequest =
            PaymentRequest(
                amount = 100.00.toBigDecimal(),
                currency = "eur",
                paymentMethod = "pm_card_visa",
            )

        val responseWrapper =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(paymentRequest)
                .post("/api/order")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().`as`(OrderCreationResponseDTO::class.java)

        val orderResponse = responseWrapper.order

        Assertions.assertThat(orderResponse).isNotNull
        Assertions.assertThat(orderResponse.totalAmount).isEqualTo(BigDecimal("200.00"))
        Assertions.assertThat(orderResponse.status.toString()).isEqualTo("CREATED")

        val memberDetails = responseWrapper.memberDetails
        Assertions.assertThat(memberDetails).isNotNull
        Assertions.assertThat(memberDetails.memberId).isEqualTo(memberId)
    }

    @Test
    fun `create order fails with empty cart`() {
        val carts = cartItemRepository.findByMemberId(memberId)
        cartItemRepository.deleteAll(carts)

        val paymentRequest =
            PaymentRequest(
                amount = 100.0.toBigDecimal(),
                currency = "eur",
                paymentMethod = "pm_card_visa",
            )

        RestAssured.given()
            .auth().oauth2(token)
            .contentType(ContentType.JSON)
            .body(paymentRequest)
            .post("/api/orders")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    private fun createCartItem() {
        val mealRequestDTO =
            MealRequestDTO(
                name = "Test Product",
                price = 100.0.toBigDecimal(),
                imageUrl = "https://example.com/image.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        val mealResponse =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(mealRequestDTO)
                .post("/api/meals")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().`as`(MealResponseDTO::class.java)

        val savedMealEntity = mealRepository.findById(mealResponse.id).get()

        val cartItem =
            CartItemEntity(
                member = member,
                meal = savedMealEntity,
                quantity = 2,
                addedAt = Instant.now(),
            )

        cartItemRepository.save(cartItem)
    }
}
