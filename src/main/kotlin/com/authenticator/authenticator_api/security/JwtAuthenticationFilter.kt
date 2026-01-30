package com.authenticator.authenticator_api.security

import com.authenticator.authenticator_api.enums.AccountStatus
import com.authenticator.authenticator_api.exceptions.UnauthorizedException
import com.authenticator.authenticator_api.models.CustomUserDetails
import com.authenticator.authenticator_api.repositories.AccountRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val accountRepository: AccountRepository,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
            ?.removePrefix("Bearer ")
            ?.trim()

        if (token == null || !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val username = jwtUtil.getUsername(token)
        val user = accountRepository.findByUsername(username)

        if (user == null) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenVersion = jwtUtil.getTokenVersion(token)
        if (tokenVersion != user.tokenVersion) {
            throw UnauthorizedException("Sessão expirada. Faça login novamente.")
        }

        if (user.accountStatus == AccountStatus.PENDING) {
            throw UnauthorizedException("A sua conta ainda não foi aprovada, aguarde a liberação.")
        }

        if (user.banned) {
            if (user.banExpiresAt == null) {
                throw UnauthorizedException("Sua conta está permanentemente bloqueada.")
            }

            if (!user.isBanExpired()) {
                throw UnauthorizedException("Conta temporariamente bloqueada. Tente mais tarde.")
            }

            user.apply {
                banned = false
                bannedAt = null
                banExpiresAt = null
                failedLoginAttempts = 0
            }
            accountRepository.save(user)
        }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name.uppercase()}"))
        val authentication = UsernamePasswordAuthenticationToken(
            CustomUserDetails(user),
            null,
            authorities
        )
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}