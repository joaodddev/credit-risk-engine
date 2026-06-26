package br.com.joaodddev.creditrisk.infrastructure.persistence

import br.com.joaodddev.creditrisk.domain.application.ApplicationId
import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class CreditApplicationRepositoryAdapter(
    private val jpaRepository: CreditApplicationJpaRepository
) : CreditApplicationRepository {

    override fun save(application: CreditApplication): CreditApplication {
        val entity = CreditApplicationJpaEntity.fromDomain(application)
        return jpaRepository.save(entity).toDomain()
    }

    override fun findById(id: ApplicationId): Optional<CreditApplication> {
        return jpaRepository.findById(id.value).map { it.toDomain() }
    }

    override fun findByApplicantDocument(document: String): List<CreditApplication> {
        return jpaRepository.findByApplicantDocument(document).map { it.toDomain() }
    }
}