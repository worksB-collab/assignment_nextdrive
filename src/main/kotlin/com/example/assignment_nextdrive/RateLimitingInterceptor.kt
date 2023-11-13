package com.example.assignment_nextdrive

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class RateLimitingInterceptor(private val rateLimiterService: RateLimiterService) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val ipAddress = exchange.request.remoteAddress?.address?.hostAddress ?: "unknown"
        return rateLimiterService.isAllowed(ipAddress)
            .flatMap { allowed ->
                if (allowed) {
                    chain.filter(exchange)
                } else {
                    exchange.response.statusCode = HttpStatus.TOO_MANY_REQUESTS
                    exchange.response.setComplete()
                }
            }
    }
}
