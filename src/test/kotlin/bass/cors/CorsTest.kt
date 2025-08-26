package bass.cors

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class CorsTest {
    lateinit var token: String

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

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `cors pre-flight request should succeed`() {
        val origin = "http://localhost:5173"
        mockMvc.perform(
            options("/api/cart")
                .header(HttpHeaders.ORIGIN, origin)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"),
        )
            .andExpect(status().isOk)
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHOD_NAMES))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION))
    }

    @Test
    fun `get cart items with token should succeed`() {
        val origin = "http://localhost:5173"
        mockMvc.perform(
            get("/api/cart")
                .header(HttpHeaders.ORIGIN, origin)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token"),
        )
            .andExpect(status().isOk)
    }

    companion object {
        private const val ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE,OPTIONS"
    }
}
