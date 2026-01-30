package com.authenticator.authenticator_api.security

import com.authenticator.authenticator_api.enums.Role
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.time.Duration

@Component
class JwtUtil(
    @Value("\${jwt.jwt-secret}") private val secret: String,
    @Value("\${jwt.jwt-expiration-days}") private val expirationDays: Long
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(username: String, role: Role, tokenVersion: Int): String {
        val now = Date()
        val expiry = Date(now.time + Duration.ofDays(expirationDays).toMillis())

        return Jwts.builder()
            .setSubject(username)
            .claim("tokenVersion", tokenVersion)
            .claim("role", role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getUsername(token: String): String {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return claims.subject
    }

    fun getTokenVersion(token: String): Int {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return claims["tokenVersion"] as Int
    }
}