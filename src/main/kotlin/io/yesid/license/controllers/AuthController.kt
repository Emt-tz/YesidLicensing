package io.yesid.license.controllers

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.yesid.license.domain.*
import io.yesid.license.models.LoginRequest
import io.yesid.license.models.LoginResponse
import jakarta.inject.Inject

@Controller("/auth")
class AuthController {

    @Inject
    lateinit var userService: UserRepository

    @Post(uri = "/login")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Login with username and password")
    fun authWithEmailAndPassword(
        @Body requestBody: LoginRequest,
    ): HttpResponse<LoginResponse> {
        val user = userService.findByUsername(requestBody.username)
            ?: return HttpResponse.notFound()

        if (user.password != requestBody.password) {
            return HttpResponse.unauthorized()
        }
        // Generate and return an access token
       // val accessToken = generateAccessToken(user)
        val accessToken = "Emt"
        return HttpResponse.ok(LoginResponse(accessToken))
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
        val createdUser = userService.save(user)
        return HttpResponse.ok(createdUser.id?.let { CreateUserResponse(it) })
    }

    @Put(uri = "/users/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Update a user")
    fun updateUser(
        @PathVariable id: Long,
        @Body requestBody: UpdateUserRequest,
    ): HttpResponse<UpdateUserResponse> {
        val existingUser = userService.findById(id)
            ?: return HttpResponse.notFound()

        val updatedUser = existingUser.get().copy(
            username = requestBody.username ?: existingUser.get().username,
            email = requestBody.email ?: existingUser.get().email,
            password = requestBody.password ?: existingUser.get().password,
        )
        userService.update(updatedUser)
        return HttpResponse.ok(updatedUser.id?.let { UpdateUserResponse(it) })
    }

    @Delete(uri = "/users/{id}")
    @Operation(summary = "Delete a user")
    fun deleteUser(
        @PathVariable id: Long,
    ): HttpResponse<Unit> {
        val existingUser = userService.findById(id)
            ?: return HttpResponse.notFound()

        userService.delete(existingUser.get())
        return HttpResponse.noContent()
    }

    @Get(uri = "/users")
    @Produces("application/json")
    @Operation(summary = "List all users")
    fun listUsers(): HttpResponse<List<User>> {
        val users = userService.findAll()
        return HttpResponse.ok(users)
    }

    @Get(uri = "/users/{id}")
    @Produces("application/json")
    @Operation(summary = "Get a user by ID")
    fun getUserById(
        @PathVariable id: Long,
    ): HttpResponse<User> {
        val user = userService.findById(id)
            ?: return HttpResponse.notFound()

        return HttpResponse.ok(user.get())
    }
}