package com.authenticator.authenticator_api.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class Hash {
    private val bcrypt = BCryptPasswordEncoder()

    fun encodePassword(rawPassword: String): String? {
        return bcrypt.encode(rawPassword)
    }

    fun checkPassword(rawPassword: String, encodedPassword: String?): Boolean {
        return bcrypt.matches(rawPassword, encodedPassword)
    }
}
