package io.yesid.license.models

import io.micronaut.core.annotation.Introspected
import io.yesid.license.domain.License

@Introspected
data class LicenseRequest(
    val userId: Long,
    val platform: String,
    val sdk: String,
    val serviceType: String,
    val authToken: String
)

@Introspected
data class LicenseResponse(
    val message: String,
    val license: License? = null
)
