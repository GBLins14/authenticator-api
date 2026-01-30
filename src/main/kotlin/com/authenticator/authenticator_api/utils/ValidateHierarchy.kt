package com.authenticator.authenticator_api.utils

import com.authenticator.authenticator_api.enums.Role
import com.authenticator.authenticator_api.exceptions.ConflictException
import com.authenticator.authenticator_api.exceptions.UnauthorizedException
import com.authenticator.authenticator_api.models.User

fun validateHierarchy(user: User, targetAccount: User) {
    if (targetAccount == user) {
        throw ConflictException("Você não pode editar sua própria conta enquanto logado.")
    }

    /*if (user.role == Role.ADMIN) {
        if (targetAccount.role == Role.ADMIN || targetAccount.role == Role.SYNDIC) {
            throw UnauthorizedException("Você não tem permissão para gerenciar um usuário com cargo igual ou superior ao seu.")
        }
    } else if (user.role == Role.BUSINESS) {
        if (targetAccount.role == Role.BUSINESS) {
            throw UnauthorizedException("Você não tem permissão para gerenciar um usuário com cargo igual ou superior ao seu.")
        }
    }*/
}