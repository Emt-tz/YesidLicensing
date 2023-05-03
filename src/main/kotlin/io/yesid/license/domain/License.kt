package io.yesid.license.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDateTime
import java.util.*

@MappedEntity
data class License(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    val userId: Long,
    val platform: String,
    val sdk: String,
    val serviceType: String,
    val licenseKey: String = UUID.randomUUID().toString(),
    val maxTransactions: Int,
    val expirationDate: LocalDateTime,
    val creationDate: LocalDateTime = LocalDateTime.now()
)

/*
    LicenseTransaction has the following attributes:

    id: a unique identifier for the transaction
    licenseId: the ID of the license used for the transaction
    platform: the platform used for the transaction (e.g., "ios", "android", "web")
    sdk: the SDK used for the transaction (e.g., "ocr", "face-enrollment", "face-authentication", "touchless")
    serviceType: the type of service used for the transaction (e.g., "basic", "premium", "enterprise")
    transactionAmount: the number of transactions made using the license for this SDK and service type
    transactionDate: the date and time when the transaction occurred (defaulting to the current date and time)

    With this LicenseTransaction entity, you can track the usage of each license by adding a new transaction
    to the database every time a license is used for a transaction.
 */
@MappedEntity
data class LicenseTransaction(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    val licenseId: Long,
    val licenseKey: String,
    val platform: String,
    val sdk: String,
    val serviceType: String,
    val transactionAmount: Int,
    val transactionDate: LocalDateTime = LocalDateTime.now()
){
    fun convertPlatformToEnum(): Platform {
        return when (platform) {
            "ios" -> Platform.IOS
            "android" -> Platform.ANDROID
            "web" -> Platform.WEB
            else -> Platform.WEB
        }
    }
}

enum class Platform {
    IOS,
    ANDROID,
    WEB
}
