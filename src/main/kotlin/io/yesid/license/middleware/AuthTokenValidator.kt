package io.yesid.license.middleware

import io.micronaut.core.async.publisher.Publishers
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.validator.TokenValidator
import io.yesid.license.repository.AuthTokenRepository
import io.yesid.license.repository.UserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import java.time.LocalDateTime

@Singleton
class AuthTokenValidator : TokenValidator {

    @Inject
    lateinit var authTokenRepository: AuthTokenRepository

    @Inject
    lateinit var userRepository: UserRepository
    override fun validateToken(token: String?, request: HttpRequest<*>?): Publisher<Authentication> {
        if (request == null || !request.path.startsWith("/license")) {
            return Publishers.empty();
        }
        val authToken = token?.let { authTokenRepository.findByToken(it) }
        if (authToken != null && LocalDateTime.now().isAfter(authToken.expires)) {
            val userDetails = authToken.userId?.let { userRepository.findById(it).get() }
            return Publishers.just(userDetails?.username?.let { Authentication.build(it, listOf(userDetails.role)) })
        }
        return Publishers.empty()
    }
}