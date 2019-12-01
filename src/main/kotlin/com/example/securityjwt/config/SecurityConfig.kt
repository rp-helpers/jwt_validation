package com.example.securityjwt.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
@EnableWebSecurity
class SecurityConfig(@Qualifier("userDetails") val userDetailsService: UserDetailsService,
                     val jwtTokenProvider: JwtTokenProvider) : WebSecurityConfigurerAdapter() {

//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin")
//                .password("{noop}123")
//                .roles("Admin")
//    }

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
//                .antMatchers("/auth").permitAll()
                .and()
                .httpBasic()
                .and()
                .apply(JwtConfigurer(jwtTokenProvider))
    }

    override fun init(web: WebSecurity) {
        super.init(web)
        web.ignoring().antMatchers("/auth")
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}