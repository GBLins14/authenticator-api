package com.authenticator.authenticator_api

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class AuthenticatorApiApplication
fun main(args: Array<String>) {
	try {
		val dotenv = Dotenv.configure().ignoreIfMissing().load()
		dotenv.entries().forEach { entry ->
			System.setProperty(entry.key, entry.value)
		}
	} catch (_: Exception) {
		println("Arquivo .env não encontrado ou erro ao carregar. Usando variáveis de ambiente do sistema.")
	}

	runApplication<AuthenticatorApiApplication>(*args)
}