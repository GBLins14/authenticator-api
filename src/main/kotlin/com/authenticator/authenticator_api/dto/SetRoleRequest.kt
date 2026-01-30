package com.authenticator.authenticator_api.dto

import com.authenticator.authenticator_api.enums.Role

data class SetRoleDto(
    val id: Long,
    val role: Role
)