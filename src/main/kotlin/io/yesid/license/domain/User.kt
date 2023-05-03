package io.yesid.license.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.util.*

@MappedEntity
data class User(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    val username: String,
    val password: String,
    val email: String,
    val role: String
)

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class CreateUserResponse(
    val userId: Long? = null,
    val username : String? = null,
    val email: String? = null,
    val role: String? = null,
    val message: String? = null,
)

data class UpdateUserRequest(
    val username: String?,
    val email: String?,
    val password: String?,
)

data class UpdateUserResponse(
    val userId: Long,
    val username : String? = null,
    val email: String? = null,
    val role: String? = null,
    val message: String? = null,
)


