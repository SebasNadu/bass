package bass.endtoend

import bass.dto.meal.MealResponseDTO
import bass.dto.member.ActiveMemberDTO
import bass.dto.product.TopProductDTO
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.RestClient
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AdminE2ETest {
    @TestConfiguration
    class TestRestClientConfig {
        @Bean
        fun restClient(builder: RestClient.Builder): RestClient = builder.build()
    }

    lateinit var token: String

    lateinit var meal: MealResponseDTO

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

        meal =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/meals/2")
                .then().log().all()
                .extract().`as`(MealResponseDTO::class.java)
    }

    @Test
    @DisplayName("GET /admin/top-meals returns 200 and meal list")
    fun getTopProducts() {
        val meals =
            RestAssured.given()
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/admin/top-meals")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("$.data", TopProductDTO::class.java)

        assertThat(meals).isNotNull()
        assertThat(meals).allSatisfy {
            assertThat(it.name).isNotBlank()
            assertThat(it.count).isGreaterThan(0)
            assertThat(Instant.now().plus(1, ChronoUnit.MINUTES))
        }
    }

    @Test
    @DisplayName("GET /admin/active-members returns 200 and member list")
    fun getActiveMembers() {
        val members =
            RestAssured.given()
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/admin/active-members")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("", ActiveMemberDTO::class.java)

        assertThat(members).isNotEmpty
        assertThat(members).allSatisfy {
            assertThat(it.id).isPositive()
            assertThat(it.email).contains("@")
        }
    }

    @Test
    @DisplayName("GET /admin/top-meals fails for non-admin token")
    fun getTopProductsUnauthorized() {
        val nonAdminToken = loginAsUser()

        RestAssured.given()
            .header("Authorization", "Bearer $nonAdminToken")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .get("/admin/top-meals")
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
    }

    private fun loginAsUser(): String {
        val loginPayload =
            mapOf(
                "email" to "user1@example.com",
                "password" to "pass",
            )

        val response =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .post("/api/members/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()

        return response.body().jsonPath().getString("accessToken")
    }
}
