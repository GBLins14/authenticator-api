package com.authenticator.authenticator_api.repositories

import com.authenticator.authenticator_api.models.PasswordResetToken
import com.authenticator.authenticator_api.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TokenRepository : JpaRepository<PasswordResetToken, UUID> {
    fun findByToken(token: String): PasswordResetToken?
    fun findByUser(user: User): PasswordResetToken?
    fun deleteByUser(user: User)
}