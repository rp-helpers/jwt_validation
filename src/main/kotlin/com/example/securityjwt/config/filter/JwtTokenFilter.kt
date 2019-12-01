package com.example.securityjwt.config.filter

import com.example.securityjwt.config.JwtTokenProvider
import com.example.securityjwt.config.KeyType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtTokenFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(request as HttpServletRequest)
        if (token != null && jwtTokenProvider.validateTokenRSA(token, KeyType.APPLICATION_TYPE)) {
            val auth: UsernamePasswordAuthenticationToken = token.let { jwtTokenProvider.getAuthentication(token, KeyType.APPLICATION_TYPE) }
            SecurityContextHolder.getContext().authentication = auth
            println("jestem tu: " + SecurityContextHolder.getContext().authentication.principal)
        }
        chain.doFilter(request, response)
    }


}