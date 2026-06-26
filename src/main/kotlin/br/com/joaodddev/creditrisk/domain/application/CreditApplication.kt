package br.com.joaodddev.creditrisk.domain.application

import br.com.joaodddev.creditrisk.domain.application.event.CreditApplicationApprovedEvent
import br.com.joaodddev.creditrisk.domain.application.event.CreditApplicationRejectedEvent
import br.com.joaodddev.creditrisk.domain.application.event.CreditApplicationSubmittedEvent
import br.com.joaodddev.creditrisk.domain.application.event.CreditApplicationUnderReviewEvent
import br.com.joaodddev.creditrisk.domain.event.DomainEvent
import java.time.LocalDateTime

class CreditApplication private constructor(
    val id: ApplicationId,
    val applicantDocument: String,
    val requestedAmount: RequestedAmount,
    var status: ApplicationStatus,
    var creditScore: CreditScore? = null,
    var rejectionReason: String? = null,
    val submittedAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {

    private val _domainEvents = mutableListOf<DomainEvent>()
    val domainEvents: List<DomainEvent> get() = _domainEvents.toList()

    companion object {
        fun submit(applicantDocument: String, requestedAmount: RequestedAmount): CreditApplication {
            require(applicantDocument.isNotBlank()) { "Applicant document is required" }

            val now = LocalDateTime.now()
            val application = CreditApplication(
                id = ApplicationId.generate(),
                applicantDocument = applicantDocument,
                requestedAmount = requestedAmount,
                status = ApplicationStatus.PENDING,
                submittedAt = now,
                updatedAt = now
            )

            application.registerEvent(
                CreditApplicationSubmittedEvent(
                    applicationId = application.id,
                    applicantDocument = applicantDocument,
                    requestedAmount = requestedAmount.toString()
                )
            )

            return application
        }

        fun reconstitute(
            id: ApplicationId,
            applicantDocument: String,
            requestedAmount: RequestedAmount,
            status: ApplicationStatus,
            creditScore: CreditScore?,
            rejectionReason: String?,
            submittedAt: LocalDateTime,
            updatedAt: LocalDateTime
        ) = CreditApplication(
            id = id,
            applicantDocument = applicantDocument,
            requestedAmount = requestedAmount,
            status = status,
            creditScore = creditScore,
            rejectionReason = rejectionReason,
            submittedAt = submittedAt,
            updatedAt = updatedAt
        )
    }

    fun startReview() {
        status = status.transitionTo(ApplicationStatus.UNDER_REVIEW)
        updatedAt = LocalDateTime.now()
        registerEvent(CreditApplicationUnderReviewEvent(applicationId = id))
    }

    fun approve(score: CreditScore) {
        status = status.transitionTo(ApplicationStatus.APPROVED)
        creditScore = score
        updatedAt = LocalDateTime.now()
        registerEvent(CreditApplicationApprovedEvent(applicationId = id, creditScore = score))
    }

    fun reject(score: CreditScore, reason: String) {
        require(reason.isNotBlank()) { "Rejection reason is required" }
        status = status.transitionTo(ApplicationStatus.REJECTED)
        creditScore = score
        rejectionReason = reason
        updatedAt = LocalDateTime.now()
        registerEvent(CreditApplicationRejectedEvent(applicationId = id, creditScore = score, reason = reason))
    }

    fun pullDomainEvents(): List<DomainEvent> {
        val events = _domainEvents.toList()
        _domainEvents.clear()
        return events
    }

    private fun registerEvent(event: DomainEvent) {
        _domainEvents.add(event)
    }
}