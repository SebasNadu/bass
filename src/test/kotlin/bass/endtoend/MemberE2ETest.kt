package bass.endtoend

import bass.dto.TokenResponseDTO
import bass.entities.TagEntity
import bass.model.Member
import bass.repositories.TagRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberE2ETest(
    @param:Autowired
    var tagRepository: TagRepository,
) {
    lateinit var token: String
    lateinit var tag: TagEntity

    @BeforeEach
    fun loginAndGetToken() {
        tag = tagRepository.save(TagEntity(name = "SomeTag"))
        val loginPayload =
            mapOf(
                "email" to "sebas@sebas.com",
                "password" to "123456",
                "tagsIds" to listOf(tag.id),
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
    fun createMember() {
//        val tag = tagRepository.save(TagEntity(name = "Vegan"))
        val registerPayload =
            mapOf(
                "name" to NAME,
                "email" to EMAIL,
                "password" to PASSWORD,
                "tagIds" to listOf(tag.id),
                "days" to setOf("MONDAY", "WEDNESDAY"),
            )
        val accessToken =
            RestAssured
                .given().log().all()
                .body(registerPayload)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().post("/api/members/register")
                .then().log().all().extract().`as`(TokenResponseDTO::class.java).accessToken

        assertThat(accessToken).isNotNull()

        val member =
            RestAssured
                .given().log().all()
                .header("Authorization", "Bearer $accessToken")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .`when`().get("/api/members/me/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().`as`(Member::class.java)

        assertThat(member.email).isEqualTo(EMAIL)
    }

    companion object {
        private const val NAME = "Sebastian"
        private const val EMAIL = "email@email.com"
        private const val PASSWORD = "1234"
    }
}
