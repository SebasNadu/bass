package bass.config

import bass.config.interceptor.CheckLoginInterceptor
import bass.config.interceptor.CheckRoleInterceptor
import bass.config.resolvers.LoginMemberArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
    private val checkLogin: CheckLoginInterceptor,
    private val checkRole: CheckRoleInterceptor,
    private val loginMemberArgumentResolver: LoginMemberArgumentResolver,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(checkLogin).addPathPatterns("/api/**", "/meals", "/admin/**")
            .excludePathPatterns("/api/members/register", "/api/members/login", "/api/tags")
        registry.addInterceptor(checkRole).addPathPatterns("/admin/**", "/api/meals/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginMemberArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        val albAddress = "bass-dev-alb-1133887665.ap-northeast-2.elb.amazonaws.com"
        registry.addMapping("/api/**")
            .allowedOrigins(
                "localhost:8080",
                "localhost:5173",
                "43.201.85.92",
                "10.0.0.204",
                albAddress,
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.LOCATION)
            .maxAge(3600)
        registry.addMapping("/swagger-ui/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.LOCATION)
            .maxAge(3600)
        registry.addMapping("/h2/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.LOCATION)
            .maxAge(3600)
    }
}
