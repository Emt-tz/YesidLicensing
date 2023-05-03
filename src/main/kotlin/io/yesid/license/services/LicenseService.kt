package io.yesid.license.services

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.yesid.license.domain.License
import io.yesid.license.domain.LicenseTransaction
import java.util.*
@JdbcRepository(dialect = Dialect.H2)
interface LicenseRepository : CrudRepository<License, Long> {
    fun save(license: License): License
    fun update(license: License): License
    override fun findAll(): List<License>
    override fun findById(id: Long): Optional<License>
    override fun delete(entity: License)
    @Query("""
    SELECT *
    FROM license
    WHERE user_id = :userId
        AND platform = :platform
        AND sdk = :sdk
        AND service_type <> :serviceType -- Exclude licenses with the same SDK and platform, but different service type
        AND expiration_date > NOW() -- Exclude expired licenses
    LIMIT 1
""")
    fun findByUserIdAndPlatformAndSdk(userId: Long, platform: String, sdk: String, serviceType: String): License?

    fun existsByUserIdAndPlatformAndSdk(userId: Long, platform: String, sdk: String, serviceType: String): Boolean {
        return findByUserIdAndPlatformAndSdk(userId, platform, sdk, serviceType) != null
    }

    @Query("""
        SELECT *
        FROM license
        WHERE user_id = :userId
        AND expiration_date > NOW() -- Exclude expired licenses
        """)
    fun findByUserId(userId: Long): List<License>

}

@JdbcRepository(dialect = Dialect.H2)
interface LicenseTransactionRepository : CrudRepository<LicenseTransaction, Long> {
    fun save(licenseTransaction: LicenseTransaction): LicenseTransaction
    override fun findById(id: Long): Optional<LicenseTransaction>
    override fun findAll(): List<LicenseTransaction>
    override fun delete(entity: LicenseTransaction)
    fun update(licenseTransaction: LicenseTransaction): LicenseTransaction
}
