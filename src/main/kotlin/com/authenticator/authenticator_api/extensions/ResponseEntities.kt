package com.authenticator.authenticator_api.extensions

import org.springframework.http.ResponseEntity

fun ResponseEntity.BodyBuilder.error(message: String): ResponseEntity<Any> {
    return this.body(mapOf("success" to false, "message" to message))
}

fun ResponseEntity.BodyBuilder.success(message: String, data: Any? = null): ResponseEntity<Any> {
    val body = mutableMapOf<String, Any>("success" to true, "message" to message)
    if (data != null) body["data"] = data
    return this.body(body)
}