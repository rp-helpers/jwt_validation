package com.example.securityjwt.controller

import com.example.securityjwt.api.JwtRequest
import com.example.securityjwt.config.JwtTokenProvider
import com.example.securityjwt.config.KeyType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EndpointController(private val jwtTokenProvider: JwtTokenProvider) {

    @GetMapping("/test")
    fun getTest() = "It's test display"

    @GetMapping("/auth")
    fun getJwtToken() = JwtRequest(jwtTokenProvider.createToken("Józef", listOf("ROLE_TEST"), KeyType.APPLICATION_TYPE))
}