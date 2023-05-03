package io.yesid.license.models

import io.micronaut.core.annotation.Introspected
import io.yesid.license.domain.AuthToken

@Introspected
data class LoginRequest(
    val username: String? = null,
    val email: String,
    val password: String
)

@Introspected
data class LoginResponse(
    val authToken: AuthToken? = null,
    val message: String
)
