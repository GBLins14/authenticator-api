package com.authenticator.authenticator_api.utils

import com.authenticator.authenticator_api.exceptions.ConflictException

fun checkDuplicate(value: Any?, message: String) {
    if (value != null) throw ConflictException(message)
}