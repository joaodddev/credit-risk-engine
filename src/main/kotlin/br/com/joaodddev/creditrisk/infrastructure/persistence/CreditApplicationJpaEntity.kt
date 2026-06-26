package br.com.joaodddev.creditrisk.infrastructure.persistence

import br.com.joaodddev.creditrisk.domain.application.ApplicationId
import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import br.com.joaodddev.creditrisk.domain.application.RequestedAmount
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "credit_applications")
class CreditApplicationJpaEntity(

    @Id
    @Column(nullable = false, updatable = false)
    val id: UUID,

    @Column(nullable = false)
    val applicantDocument: String,

    @Column(nullable = false, precision = 19, scale = 2)
    val requestedAmount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ApplicationStatus,

    @Column
    val creditScore: Int?,

    @Column
    val rejectionReason: String?,

    @Column(nullable = false, updatable = false)
    val submittedAt: LocalDateTime,

    @Column(nullable = false)
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(application: CreditApplication) = CreditApplicationJpaEntity(
            id = application.id.value,
            applicantDocument = application.applicantDocument,
            requestedAmount = application.requestedAmount.value,
            status = application.status,
            creditScore = application.creditScore?.value,
            rejectionReason = application.rejectionReason,
            submittedAt = application.submittedAt,
            updatedAt = application.updatedAt
        )
    }

    fun toDomain() = CreditApplication.reconstitute(
        id = ApplicationId.of(id.toString()),
        applicantDocument = applicantDocument,
        requestedAmount = RequestedAmount.of(requestedAmount),
        status = status,
        creditScore = creditScore?.let { CreditScore.of(it) },
        rejectionReason = rejectionReason,
        submittedAt = submittedAt,
        updatedAt = updatedAt
    )
}