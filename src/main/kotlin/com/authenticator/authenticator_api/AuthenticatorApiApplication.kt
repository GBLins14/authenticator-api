package com.authenticator.authenticator_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class AuthenticatorApiApplication

fun main(args: Array<String>) {
    runApplication<AuthenticatorApiApplication>(*args)
}