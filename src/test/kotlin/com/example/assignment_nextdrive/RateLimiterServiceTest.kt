package com.example.assignment_nextdrive

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier
import java.time.Duration

@ExtendWith(SpringExtension::class)
class RateLimiterServiceTest {

    @Test
    fun `test isAllowed when requests are allowed`() {
        val rateLimiterService = RateLimiterService()
        val ip = "127.0.0.1"

        for (i in 1..9) {
            rateLimiterService.isAllowed(ip).block(Duration.ZERO)
        }

        // The 10th request should still be allowed
        val resultMono = rateLimiterService.isAllowed(ip)
        StepVerifier.create(resultMono)
            .expectNext(true)
            .verifyComplete()
    }

    @Test
    fun `test isAllowed when requests are not allowed`() {
        val rateLimiterService = RateLimiterService()
        val ip = "127.0.0.1"

        for (i in 1..10) {
            rateLimiterService.isAllowed(ip).block(Duration.ZERO)
        }

        // The 11th request should not be allowed
        val resultMono = rateLimiterService.isAllowed(ip)
        StepVerifier.create(resultMono)
            .expectNext(false)
            .verifyComplete()
    }
}
