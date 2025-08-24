package bass.endtoend

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@Disabled("Disabled by default to avoid external API calls and incur costs. Enable when needed.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MealNaturalSearchE2ETest {
    lateinit var token: String

    @BeforeEach
    fun loginAndGetToken() {
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
        assertThat(token).isNotBlank
    }

    @Test
    fun `natural search returns meals matching tags`() {
        val request =
            mapOf(
                "userText" to "I want a healthy vegan bowl",
                "requireAllTags" to false,
                "maxTags" to 8,
            )

        val response =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(request)
                .`when`()
                .post("/api/meals/search/natural")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()

        val selectedTags: List<String> = response.getList("selectedTags")
        val meals: List<Map<String, Any>> = response.getList("meals")

        assertEquals(listOf("healthy", "vegan", "bowl"), selectedTags)
        assertTrue(meals.isNotEmpty())
        assertTrue(
            meals.any {
                it["name"] == "Vegan Tofu Scramble Bowl" ||
                    it["name"] == "Quinoa Buddha Bowl" || it["name"] == "Salmon Poke Bowl"
            },
        )
    }

    @Test
    fun `natural search with no matching tags returns empty result`() {
        val request =
            mapOf(
                "userText" to "I want chocolate dessert",
                "requireAllTags" to false,
                "maxTags" to 8,
            )

        val response =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(request)
                .`when`()
                .post("/api/meals/search/natural")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()

        val selectedTags: List<String> = response.getList("selectedTags")
        val meals: List<Map<String, Any>> = response.getList("meals")

        assertTrue(selectedTags.isEmpty())
        assertTrue(meals.isEmpty())
    }
}
