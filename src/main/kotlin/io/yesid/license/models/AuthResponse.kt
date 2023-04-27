package io.yesid.license.models

import io.micronaut.core.annotation.Introspected

@Introspected
data class LoginRequest(
    val username: String,
    val password: String
)

@Introspected
data class LoginResponse(
    val message: String
)
