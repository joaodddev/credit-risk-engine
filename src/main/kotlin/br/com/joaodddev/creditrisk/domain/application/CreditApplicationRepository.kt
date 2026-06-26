package br.com.joaodddev.creditrisk.domain.application

import java.util.Optional

interface CreditApplicationRepository {
    fun save(application: CreditApplication): CreditApplication
    fun findById(id: ApplicationId): Optional<CreditApplication>
    fun findByApplicantDocument(document: String): List<CreditApplication>
}