package com.authenticator.authenticator_api.utils

fun generateCode(): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val randomString = (1..6) // Tamanho de 6 digitos
        .map { characters.random() }
        .joinToString("")

    return randomString
}