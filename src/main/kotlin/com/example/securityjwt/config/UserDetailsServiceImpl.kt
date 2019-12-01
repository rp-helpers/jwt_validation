package com.example.securityjwt.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component("userDetails")
class UserDetailsServiceImpl : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        if (username == "Józef") {
            val authorities = mutableListOf<GrantedAuthority>()
            authorities.add(SimpleGrantedAuthority("ROLE_TEST"))
            return User
                    .withUsername("Józef")
                    .password("123")
                    .authorities(authorities)
                    .build()

        } else {
            throw RuntimeException("User is not exist")
        }
    }
}