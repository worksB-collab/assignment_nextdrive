package com.example.assignment_nextdrive

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimiterService {

    private val requestCounts = ConcurrentHashMap<String, Int>()
    private val lastRequestTimes = ConcurrentHashMap<String, Instant>()

    fun isAllowed(ip: String): Mono<Boolean> {
        return Mono.fromSupplier {
            val currentTime = Instant.now()

            requestCounts.keys.removeIf { lastRequestTimes[it]?.isBefore(currentTime.minusSeconds(60)) ?: true }

            val count = requestCounts.compute(ip) { _, value -> (value ?: 0) + 1 } ?: 1
            lastRequestTimes[ip] = currentTime

            count <= 10
        }
    }
}