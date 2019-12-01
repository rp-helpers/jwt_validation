package com.example.securityjwt.config

import com.example.securityjwt.config.filter.JwtTokenFilter
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


class JwtConfigurer(private val jwtTokenProvider: JwtTokenProvider) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(builder: HttpSecurity) {
        super.configure(builder)
        val customFilter: JwtTokenFilter = JwtTokenFilter(jwtTokenProvider)
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}