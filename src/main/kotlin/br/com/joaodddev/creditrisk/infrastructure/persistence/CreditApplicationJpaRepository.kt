package br.com.joaodddev.creditrisk.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CreditApplicationJpaRepository : JpaRepository<CreditApplicationJpaEntity, UUID> {
    fun findByApplicantDocument(document: String): List<CreditApplicationJpaEntity>
}