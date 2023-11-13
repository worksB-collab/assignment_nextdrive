package com.example.assignment_nextdrive

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RateLimitingController {

    @GetMapping("/resource")
    fun getResource(): String {
        return "This is the protected resource."
    }
}
