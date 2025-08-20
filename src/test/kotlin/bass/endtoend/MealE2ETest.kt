package bass.endtoend

import bass.dto.PageResponseDTO
import bass.dto.meal.MealRequestDTO
import bass.dto.meal.MealResponseDTO
import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MealE2ETest {
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
    fun getMeals() {
        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(ContentType.JSON)
                .`when`().get("/api/meals")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        val page = response.body().`as`(object : TypeRef<PageResponseDTO<MealResponseDTO>>() {})
        assertThat(page.content).isNotEmpty()
        assertThat(page.content.size).isEqualTo(10)
    }

    @Test
    fun getMeal() {
        val productDTO =
            MealRequestDTO(
                name = "TV",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )
        val id =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(productDTO)
                .post("/api/meals")
                .then().extract().jsonPath().getLong("id")

        val response =
            RestAssured.given()
                .get("/api/meals/$id")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("TV")
        assertThat(response.body().jsonPath().getFloat("price")).isEqualTo(99.99f)
    }

    @Test
    fun getMeal_notFound() {
        val response =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/meals/999999")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun createMeal() {
        val newMealDTO =
            MealRequestDTO(
                name = "Monitor",
                price = 150.0,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(newMealDTO)
                .`when`().post("/api/meals")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("Monitor")
        assertThat(response.body().jsonPath().getFloat("price")).isEqualTo(150.0f)
    }

    @Test
    fun `Should return error when product name use invalid characters`() {
        val newMealDTO =
            MealRequestDTO(
                name = "!@#$%^&*()_+}{",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(newMealDTO)
                .`when`().post("/api/meals")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body().jsonPath().getInt("status")).isEqualTo(400)
        assertThat(
            response.body().jsonPath().getString("errors.find { it.field = 'name' }.message"),
        ).isEqualTo("Invalid characters in product name.")
    }

    @Test
    fun `Should return error when product name is bigger than 15 characters`() {
        val newMealDTO =
            MealRequestDTO(
                name = "SpeakersareLovemyDearDearDear",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(newMealDTO)
                .`when`().post("/api/meals")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body().jsonPath().getInt("status")).isEqualTo(400)
        assertThat(
            response.body().jsonPath().getString("errors.find { it.field = 'name' }.message"),
        ).isEqualTo("The product name must contain between 1 and 15 characters")
    }

    @Test
    fun `Should return error when product name already exists`() {
        val dto =
            MealRequestDTO(
                name = "Speaker",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        RestAssured.given().auth().oauth2(token)
            .contentType(ContentType.JSON).body(dto).post("/api/meals")

        val duplicate =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(dto)
                .post("/api/meals")
                .then().extract()

        assertThat(duplicate.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(duplicate.body().jsonPath().getString("message")).contains("already exists")
    }

    @Test
    fun `Should return error when product price is negative value`() {
        val newMealDTO =
            MealRequestDTO(
                name = "Speaker2",
                price = -99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(newMealDTO)
                .`when`().post("/api/meals")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(response.body().jsonPath().getInt("status")).isEqualTo(400)
        assertThat(
            response.body().jsonPath().getString("errors.find { it.field == 'price' }.message"),
        ).contains("must be greater than zero")
    }

    @Test
    fun updateMeal() {
        val created =
            MealRequestDTO(
                name = "Speaker3",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )
        val id =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(created)
                .post("/api/meals")
                .then().extract().jsonPath().getLong("id")

        val updated =
            MealRequestDTO(
                name = "Gaming Mouse",
                price = 45.0,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(updated)
                .put("/api/meals/$id")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("Gaming Mouse")
        assertThat(response.body().jsonPath().getFloat("price")).isEqualTo(45.0f)
    }

    @Test
    fun patchMeal() {
        val created =
            MealRequestDTO(
                name = "Tv",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )
        val id =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(created)
                .post("/api/meals")
                .then().extract().jsonPath().getLong("id")

        val patch = mapOf("price" to 249.0)

        val response =
            RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(patch)
                .patch("/api/meals/$id")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().jsonPath().getFloat("price")).isEqualTo(249.0f)
    }

    @Test
    fun deleteMeal() {
        val created =
            MealRequestDTO(
                name = "Toilet",
                price = 99.99,
                imageUrl = "https://example.com/speaker.jpg",
                quantity = 4,
                description = "description",
                tagsIds = setOf(1L, 2L),
            )
        val id =
            RestAssured.given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(created)
                .post("/api/meals")
                .then().extract().jsonPath().getLong("id")

        val deleteResponse =
            RestAssured.given()
                .auth().oauth2(token)
                .delete("/api/meals/$id")
                .then().log().all().extract()

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())

        val getResponse =
            RestAssured.given()
                .auth().oauth2(token)
                .get("/api/meals/$id")
                .then().log().all().extract()

        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
    }
}
