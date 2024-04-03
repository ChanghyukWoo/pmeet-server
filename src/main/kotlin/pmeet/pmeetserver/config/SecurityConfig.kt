package pmeet.pmeetserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers

@EnableWebFluxSecurity
@Configuration
class SecurityConfig {
  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Order(Ordered.HIGHEST_PRECEDENCE)
  @Bean
  open fun authApiFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http {
      cors { disable() }
      csrf { disable() }
      formLogin { disable() }
      httpBasic { disable() }
      securityMatcher(PathPatternParserServerWebExchangeMatcher("/api/v1/auth/**"))
      authorizeExchange {
        authorize("/api/v1/auth/**", permitAll)
      }
    }
  }

  @Bean
  fun filterChain(
    http: ServerHttpSecurity,
    converter: JwtServerAuthenticationConverter,
    handler: ServerAuthenticationFailureHandler,
    authManager: JwtAuthenticationManager
  ): SecurityWebFilterChain {
    val filter = AuthenticationWebFilter(authManager)
    filter.setServerAuthenticationConverter(converter)
    filter.setAuthenticationFailureHandler(handler)

    return http {
      cors { disable() }
      csrf { disable() }
      formLogin { disable() }
      httpBasic { disable() }
      authorizeExchange {
        authorize(pathMatchers("/v3/api-docs/**", "/webjars/**"), permitAll)
        authorize(anyExchange, authenticated)
      }
      addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
    }
  }
}
