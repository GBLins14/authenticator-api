package com.authenticator.authenticator_api.security

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Component
class RateLimitFilter(
    @Value("\${app.max-requests-per-minutes}") private val MAX_REQUESTS_PER_MINUTES: Long,
) : Filter {
    private val cache = ConcurrentHashMap<String, Bucket>()

    private val protectedRotes = listOf(
        "/api/"
    )

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val path = httpRequest.requestURI

        if (path.startsWith("/api/webhooks")) {
            chain.doFilter(request, response)
            return
        }

        val protected = protectedRotes.any { path.startsWith(it) }

        if (protected) {
            val clientIp = getClientIp(httpRequest)
            val bucket = cache.computeIfAbsent(clientIp) { createNewBucket() }

            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response)
            } else {
                httpResponse.status = HttpStatus.TOO_MANY_REQUESTS.value()
                httpResponse.writer.write("Muitas tentativas. Aguarde 1 minuto.")
                return
            }
        } else {
            chain.doFilter(request, response)
        }
    }

    private fun createNewBucket(): Bucket {
        val limit = Bandwidth.builder()
            .capacity(MAX_REQUESTS_PER_MINUTES)
            .refillGreedy(MAX_REQUESTS_PER_MINUTES, Duration.ofMinutes(1))
            .build()
        return Bucket.builder().addLimit(limit).build()
    }

    private fun getClientIp(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        if (xForwardedFor != null && xForwardedFor.isNotEmpty()) {
            return xForwardedFor.split(",")[0].trim()
        }
        return request.remoteAddr
    }
}