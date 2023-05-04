package io.yesid.license.repository

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.yesid.license.domain.LicenseTransaction
import java.util.*

@JdbcRepository(dialect = Dialect.H2)
interface LicenseTransactionRepository : CrudRepository<LicenseTransaction, Long> {
    fun save(licenseTransaction: LicenseTransaction): LicenseTransaction
    override fun findById(id: Long): Optional<LicenseTransaction>
    override fun findAll(): List<LicenseTransaction>
    override fun delete(entity: LicenseTransaction)
    fun update(licenseTransaction: LicenseTransaction): LicenseTransaction
}
