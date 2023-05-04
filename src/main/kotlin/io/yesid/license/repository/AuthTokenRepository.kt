package io.yesid.license.repository

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.yesid.license.domain.AuthToken
import java.util.*


@JdbcRepository(dialect = Dialect.H2)
interface AuthTokenRepository : CrudRepository<AuthToken, Long> {
    fun findByToken(token: String): AuthToken?
    fun save(authToken: AuthToken): AuthToken
    override fun delete(authToken: AuthToken)

    @Query("SELECT COUNT(*) > 0 FROM auth_token WHERE token = :token AND user_id = :userId AND expires > NOW()")
    fun isTokenActive(token: String, userId: Long): Boolean

    @Query("SELECT * FROM auth_token WHERE user_id = :userId")
    fun findTokenByUserId(userId: Long): AuthToken?

    @Query("DELETE FROM auth_token WHERE user_id = :userId")
    fun deleteTokenByUserId(userId: Long)

}




