package io.yesid.license.controllers

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.yesid.license.domain.*
import io.yesid.license.models.LoginRequest
import io.yesid.license.models.LoginResponse
import io.yesid.license.repository.AuthTokenRepository
import io.yesid.license.repository.UserRepository
import jakarta.inject.Inject
import java.util.*

@Controller("/auth")
@Secured(SecurityRule.IS_AUTHENTICATED)
class AuthController {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var authTokenRepository: AuthTokenRepository

    @Post(uri = "/login")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Login with username and password")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun authWithEmailAndPassword(
        @Body requestBody: LoginRequest,
    ): HttpResponse<LoginResponse> {
        // check user by username or email
        val user = requestBody.username?.let { userRepository.findByUsername(it) }
            ?: userRepository.findByEmail(requestBody.email)
            ?: return HttpResponse.badRequest(
                LoginResponse(
                    message = "User with username or email does not exist."
                )
            )

        if (user.password != requestBody.password) {
            return HttpResponse.badRequest(
                LoginResponse(
                    message = "Invalid password."
                )
            )
        }
        val authToken = AuthToken(userId = user.id)
        if (user.id?.let { authTokenRepository.findTokenByUserId(userId = it) } != null)
            authToken.userId?.let { authTokenRepository.deleteTokenByUserId(it) }
        authTokenRepository.save(authToken)
        return HttpResponse.ok(
            LoginResponse(
                authToken = authToken,
                message = "Login successful."
            )
        )
    }

    @Post(uri = "/register")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Register a new user")
    fun registerUser(
        @Body requestBody: CreateUserRequest,
    ): HttpResponse<CreateUserResponse> {
        val user = User(
            username = requestBody.username,
            email = requestBody.email,
            password = requestBody.password,
            role = "USER"
        )
        // before saving a user check if the username or email already exists respond with user exists
        if (userRepository.findByUsername(user.username) != null || userRepository.findByEmail(user.email) != null) {
            return HttpResponse.badRequest(CreateUserResponse(message = "User with username or email already exists."))
        }
        val createdUser = userRepository.save(user)
        return HttpResponse.ok(
            CreateUserResponse(
                userId = createdUser.id,
                username = createdUser.username,
                email = createdUser.email,
                role = createdUser.role,
                message = "User created successfully."
            )
        )
    }

    @Put(uri = "/users/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Update a user")
    fun updateUser(
        @PathVariable id: Long,
        @Body requestBody: UpdateUserRequest,
    ): HttpResponse<UpdateUserResponse> {
        val existingUser = userRepository.findById(id)
            ?: return HttpResponse.notFound()

        val updatedUser = existingUser.get().copy(
            username = requestBody.username ?: existingUser.get().username,
            email = requestBody.email ?: existingUser.get().email,
            password = requestBody.password ?: existingUser.get().password,
        )
        userRepository.update(updatedUser)
        return HttpResponse.ok(updatedUser.id?.let { UpdateUserResponse(it) })
    }

    @Delete(uri = "/users/{id}")
    @Operation(summary = "Delete a user")
    fun deleteUser(
        @PathVariable id: Long,
    ): HttpResponse<Unit> {
        val existingUser = userRepository.findById(id)
            ?: return HttpResponse.notFound()

        userRepository.delete(existingUser.get())
        return HttpResponse.noContent()
    }

    @Get(uri = "/users")
    @Produces("application/json")
    @Operation(summary = "List all users")
    fun listUsers(): HttpResponse<List<User>> {
        val users = userRepository.findAll()
        return HttpResponse.ok(users)
    }

    @Get(uri = "/users/{id}")
    @Produces("application/json")
    @Operation(summary = "Get a user by ID")
    fun getUserById(
        @PathVariable id: Long,
    ): HttpResponse<User> {
        val user = userRepository.findById(id)
            ?: return HttpResponse.notFound()

        return HttpResponse.ok(user.get())
    }

}