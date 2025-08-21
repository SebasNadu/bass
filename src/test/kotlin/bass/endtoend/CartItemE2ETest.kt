package bass.endtoend

import bass.dto.CartItemRequestDTO
import bass.dto.CartItemResponseDTO
import bass.dto.PageResponseDTO
import bass.dto.meal.MealResponseDTO
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDateTime

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartItemE2ETest {
    lateinit var token: String
    private val mealId: Long = 1L
    private val request get() = CartItemRequestDTO(mealId = mealId, quantity = 2)

    @BeforeEach
    fun setup() {
        val loginPayload =
            mapOf(
                "email" to "sebas@sebas.com",
                "password" to "123456",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .post("/api/members/login")
                .then().extract()

        token = response.body().jsonPath().getString("accessToken")
        assertThat(token).isNotBlank()
    }

    @Test
    fun `add cart item`() {
        val cartItem = addCartItemAndReturn()

        assertThat(cartItem.meal).isInstanceOf(MealResponseDTO::class.java)
        assertThat(cartItem.meal.id).isEqualTo(mealId)
        assertThat(cartItem.quantity).isEqualTo(2)
        assertThat(cartItem.addedAt).isBefore(LocalDateTime.now().plusMinutes(1))
    }

    @Test
    fun `get cart items`() {
        val addedItem = addCartItemAndReturn()

        val items =
            RestAssured.given()
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/cart")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList("", CartItemResponseDTO::class.java)

        assertThat(items).hasSize(1)

        val cartItem = items.first()
        with(cartItem) {
            assertThat(quantity).isEqualTo(addedItem.quantity)
            assertThat(meal.id).isEqualTo(mealId)
            assertThat(addedAt).isBefore(LocalDateTime.now().plusMinutes(1))
            assertThat(addedAt).isAfter(LocalDateTime.now().minusDays(1))
        }
    }

    @Test
    fun `delete cart item`() {
        addCartItemAndReturn()
        RestAssured.given()
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .get("/api/meals")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().body().`as`(object : TypeRef<PageResponseDTO<MealResponseDTO>>() {})

        RestAssured.given()
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .delete("/api/cart")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())

        val items =
            RestAssured.given()
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/cart")
                .then().extract().body().jsonPath().getList("", MealResponseDTO::class.java)

        assertThat(items).isEmpty()
    }

    private fun addCartItemAndReturn(): CartItemResponseDTO {
        return RestAssured.given()
            .header("Authorization", "Bearer $token")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .post("/api/cart")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`(CartItemResponseDTO::class.java)
    }
}
