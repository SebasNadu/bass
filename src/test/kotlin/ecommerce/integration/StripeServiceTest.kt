package ecommerce.integration

import ecommerce.exception.PaymentFailedException
import ecommerce.infrastructure.StripeClient
import ecommerce.model.PaymentRequest
import ecommerce.model.StripeResponse
import ecommerce.services.stripe.StripeService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@SpringBootTest
class StripeServiceTest {

    @Autowired
    private lateinit var stripeService: StripeService

    @MockBean
    private lateinit var stripeClient: StripeClient

    @BeforeEach
    fun setup() {
        Mockito.`when`(
            stripeClient.createPaymentIntent(any<MultiValueMap<String, String>>())
        ).thenReturn(
            StripeResponse(
                id = "pi_123",
                amount = 1000,
                currency = "usd",
                status = "requires_payment_method",
                cancellationReason = null,
                clientSecret = "secret_123",
                customer = "cus_123",
                created = System.currentTimeMillis() / 1000,
                paymentMethod = "pm_card_visa",
                latestCharge = "ch_123"
            )
        )
    }

    @Test
    fun `createPaymentIntent returns valid response from Stripe`() {
        val paymentRequest = PaymentRequest(
            amount = 1000.0,
            currency = "usd",
            paymentMethod = "pm_card_visa"
        )

        val response = stripeService.createPaymentIntent(paymentRequest)

        println(response)

        assertNotNull(response)
        assertEquals(1000.0, response.amountDecimal)
        assertEquals("usd", response.currency?.lowercase())
        assertTrue(
            response.status.equals("requires_payment_method", ignoreCase = true) ||
                    response.status.equals("succeeded", ignoreCase = true) ||
                    response.status.equals("requires_action", ignoreCase = true)
        )
    }

//    @Test
//    fun `createPaymentIntent fails with radar block payment method`() {
//        val paymentRequest = PaymentRequest(
//            amount = 1000.0,
//            currency = "usd",
//            paymentMethod = "pm_card_radarBlock"
//        )
//
//        Mockito.`when`(
//            stripeClient.createPaymentIntent(
//                Mockito.argThat { paymentMethod == "pm_card_radarBlock" }
//            )
//        ).thenThrow(PaymentFailedException("Stripe payment initialization failed"))
//
//
//        val exception = assertThrows<PaymentFailedException> {
//            stripeService.createPaymentIntent(paymentRequest)
//        }
//
//        println("Expected failure: ${exception.message}")
//        assertTrue(exception.message!!.contains("Stripe payment initialization failed"))
//    }
}
