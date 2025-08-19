package bass.endtoend

import bass.dto.member.ActiveMemberDTO
import bass.dto.MealDTO
import bass.dto.TopProductDTO
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.RestClient
import java.time.LocalDateTime
import kotlin.jvm.java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class AdminE2ETest {
    @TestConfiguration
    class TestRestClientConfig {
        @Bean
        fun restClient(builder: RestClient.Builder): RestClient = builder.build()
    }

    lateinit var token: String

    lateinit var product: MealDTO

    @Autowired
    lateinit var restClient: RestClient

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

        product =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .get("/api/meals/2")
                .then().log().all()
                .extract().`as`(MealDTO::class.java)
    }

    @Test
    @DisplayName("GET /admin/top-products returns 200 and product list")
    fun getTopProducts() {
        val products =
            RestAssured.given()
                .header("Authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/admin/top-products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList("$.data", TopProductDTO::class.java)

        assertThat(products).isNotNull()
        assertThat(products).allSatisfy {
            assertThat(it.name).isNotBlank()
            assertThat(it.count).isGreaterThan(0)
            assertThat(it.mostRecentAddedAt).isBefore(LocalDateTime.now().plusMinutes(1))
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
    @DisplayName("GET /admin/top-products fails for non-admin token")
    fun getTopProductsUnauthorized() {
        val nonAdminToken = loginAsUser()

        RestAssured.given()
            .header("Authorization", "Bearer $nonAdminToken")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .get("/admin/top-products")
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
