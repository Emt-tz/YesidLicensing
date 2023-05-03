package io.yesid.license.services

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.yesid.license.domain.User
import java.util.*

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
