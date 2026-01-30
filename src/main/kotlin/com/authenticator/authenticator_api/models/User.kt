package com.authenticator.authenticator_api.models

import com.authenticator.authenticator_api.enums.AccountStatus
import com.authenticator.authenticator_api.enums.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.Instant

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val cpf: String,

    var fullName: String,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    var phone: String,

    @JsonIgnore
    @Column(nullable = false)
    var hashedPassword: String?,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var accountStatus: AccountStatus,

    @Column(nullable = false)
    var banned: Boolean = false,

    var bannedAt: Instant? = null,

    var banExpiresAt: Instant? = null,

    @Column(nullable = false)
    var failedLoginAttempts: Int = 0,

    @Column(nullable = false)
    var tokenVersion: Int = 0,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now()
) {
    @PrePersist
    @PreUpdate
    fun formatData() {
        this.fullName = this.fullName.uppercase()
        this.username = this.username.lowercase()
        this.email = this.email.lowercase()
    }

    fun isBanExpired(): Boolean {
        if (banExpiresAt == null) return false
        return Instant.now().isAfter(banExpiresAt)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "User(id=$id, username='$username', email='$email', role=$role)"
    }
}
