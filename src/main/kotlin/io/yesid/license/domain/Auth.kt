package io.yesid.license.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDateTime
import java.util.*

@MappedEntity
data class AuthToken(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var userId: Long? = null,
    val token: String = UUID.randomUUID().toString(),
    val expires: LocalDateTime =  LocalDateTime.now().plusHours(1),
)
