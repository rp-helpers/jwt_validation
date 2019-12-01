package com.example.securityjwt.config

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(@Qualifier("userDetails") val userDetailsService: UserDetailsService,
                       private val serverCryptoKeyRepository: ServerCryptoKeyRepository) {

    var secretKey: String = SECRET
    val validityInMiliseconds: Long = EXPIRATION_TIME

    fun createToken(username: String?, roles: List<String>, keyType: KeyType): String {
        val claims: Claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles

        val currentDate: Date = Date()
        val validity: Date = Date(currentDate.time + validityInMiliseconds)

        return TOKEN_PREFIX + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.RS256, serverCryptoKeyRepository.find(keyType).privateKey)
                .compact()
    }

    fun getAuthentication(token: String, keyType: KeyType): UsernamePasswordAuthenticationToken {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getUsername(token, keyType))!!
        return UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, userDetails.authorities)
    }

    fun getUsername(token: String, keyType: KeyType) = Jwts.parser()
            .setSigningKey(serverCryptoKeyRepository.find(keyType).publicKey)
            .parseClaimsJws(token)
            .body
            .subject

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader(HEADER_AUTH)
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX))
            return bearerToken.substring(7, bearerToken.length)
        return null
    }

    fun getAuthenticationRSA(token: String, keyType: KeyType): UsernamePasswordAuthenticationToken {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getUsernameRSA(token, keyType))!!
        return UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, userDetails.authorities)
    }

    fun getUsernameRSA(token: String, keyType: KeyType) = Jwts.parser()
            .setSigningKey(serverCryptoKeyRepository.find(keyType).publicKey)
            .parseClaimsJws(token)
            .body
            .subject

    fun validateTokenRSA(token: String, keyType: KeyType): Boolean {
        try {
            val claims: Jws<Claims> = Jwts.parser()
                    .setSigningKey(serverCryptoKeyRepository.find(keyType).publicKey)
                    .parseClaimsJws(token)
            if (claims.body.expiration.before(Date()))
                return false
            return true
        } catch (e: JwtException) {
//            throw InvalidJwtAuthenticationException
            throw RuntimeException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Expired or invalid JWT token")
        }
    }

}