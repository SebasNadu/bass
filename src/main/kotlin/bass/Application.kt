package bass

import bass.config.security.JwtProperties
import bass.config.stripe.StripeProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class, StripeProperties::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
