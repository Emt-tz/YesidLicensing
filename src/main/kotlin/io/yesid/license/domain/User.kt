package io.yesid.license.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
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
    val id: Long,
)

data class UpdateUserRequest(
    val username: String?,
    val email: String?,
    val password: String?,
)

data class UpdateUserResponse(
    val id: Long,
)


@JdbcRepository(dialect = Dialect.H2)
interface UserRepository: CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun save(user: User): User

    fun update(user: User): User

    override fun findAll(): List<User>

    override fun findById(id: Long): Optional<User>
    override fun delete(user: User)


}
