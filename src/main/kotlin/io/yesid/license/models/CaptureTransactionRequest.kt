package io.yesid.license.models

import io.micronaut.core.annotation.Introspected
import io.yesid.license.domain.License
import io.yesid.license.domain.LicenseTransaction

@Introspected
data class CaptureTransactionRequest(
    val transactionType: String,
    val transactionAmount: Double
)

@Introspected
data class CaptureTransactionResponse(
    val message: String,
    val licenseTransaction: LicenseTransaction
)

@Introspected
data class ValidateLicenseRequest(
    val licenseKey: String
)

@Introspected
data class ValidateLicenseResponse(
    val message: String,
    val license: License?,
    val remainingTransactions: Int?
)

