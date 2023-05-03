package io.yesid.license.middleware

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.yesid.license.services.AuthTokenRepository
import io.yesid.license.services.UserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton
class TokenAuthMiddleware : AuthenticationProvider {

    @Inject
    lateinit var userService: UserRepository

    @Inject
    lateinit var authTokenService: AuthTokenRepository
    override fun authenticate(
        @Nullable httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>?
    ): Publisher<AuthenticationResponse>? {
        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            val userId = authenticationRequest?.identity.toString().toLong()
            val token = authenticationRequest?.secret.toString()
            val authToken = authTokenService.isTokenActive(token, userId)
            if (authToken) {
                emitter.next(AuthenticationResponse.success(authToken.toString()))
                emitter.complete()
            } else {
                emitter.error(AuthenticationResponse.exception())
            }

        }, FluxSink.OverflowStrategy.ERROR)
    }
}




