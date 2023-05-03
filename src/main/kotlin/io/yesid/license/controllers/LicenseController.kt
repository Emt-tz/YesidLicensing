package io.yesid.license.controllers

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.yesid.license.domain.License
import io.yesid.license.models.*
import io.yesid.license.services.*
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.util.*

@Controller("/license")
@Secured(SecurityRule.IS_AUTHENTICATED)
class LicenseController {

    @Inject
    lateinit var userService: UserRepository

    @Inject
    lateinit var licenseService: LicenseRepository

    @Inject
    lateinit var licenseTransactionService: LicenseTransactionRepository

    @Inject
    lateinit var authTokenRepository: AuthTokenRepository

    // Route to create a new license
    @Post(uri = "/create")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Create a new license")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun createLicense(
        @Body requestBody: LicenseRequest,// extract the user id from the request attribute
    ): HttpResponse<LicenseResponse> {
        // Check if a license already exists for the requested service type with the same key and has not expired
        val existingLicense = licenseService.existsByUserIdAndPlatformAndSdk(
            requestBody.userId,
            requestBody.platform,
            requestBody.sdk,
            requestBody.serviceType
        )
        if (existingLicense) {
            return HttpResponse.badRequest<LicenseResponse>(
                LicenseResponse("A license already exists for this user and SDK.")
            )
        }
        // Retrieve the user by ID
        val user = userService.findById(requestBody.userId).orElse(null)
            ?: return HttpResponse.badRequest<LicenseResponse>(
                LicenseResponse("User with ID ${requestBody.userId} does not exist.")
            )

        // Create the new license
        val license = user.id?.let {
            License(
                userId = it,
                platform = requestBody.platform,
                sdk = requestBody.sdk,
                serviceType = requestBody.serviceType,
                maxTransactions = 100, // default max transactions
                expirationDate = LocalDateTime.now().plusDays(30)
            )
        }

        // Save the new license
        val savedLicense = license?.let { licenseService.save(it) }

        return HttpResponse.ok(LicenseResponse("License created successfully.", savedLicense))
    }

    // Route to get all licenses for a user ID
    @Get(uri = "/list/{userId}")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "List all licenses for a user ID")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun getLicensesByUserId(userId: Long): HttpResponse<List<License>> {
        // Retrieve the licenses for the given user ID
        val licenses = licenseService.findByUserId(userId)
        return HttpResponse.ok(licenses)
    }



}
