package io.yesid.license.middleware

import io.micronaut.security.token.reader.HttpHeaderTokenReader
import io.yesid.license.repository.AuthTokenRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AuthTokenReader : HttpHeaderTokenReader() {
    private val X_API_TOKEN = "X-API-KEY"

    @Inject
    lateinit var authTokenRepository: AuthTokenRepository
    override fun getPrefix(): String {
        return ""
    }

    override fun getHeaderName(): String {
        return X_API_TOKEN
    }


}







