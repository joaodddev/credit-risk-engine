package br.com.joaodddev.creditrisk.application.usecase

import br.com.joaodddev.creditrisk.application.exception.CreditApplicationNotFoundException
import br.com.joaodddev.creditrisk.application.port.out.CreditScoreProvider
import br.com.joaodddev.creditrisk.application.port.out.DomainEventPublisher
import br.com.joaodddev.creditrisk.domain.application.ApplicationId
import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import br.com.joaodddev.creditrisk.domain.policy.PolicyResult
import br.com.joaodddev.creditrisk.domain.policy.RiskPolicy

class EvaluateCreditApplicationUseCase(
    private val repository: CreditApplicationRepository,
    private val scoreProvider: CreditScoreProvider,
    private val policies: List<RiskPolicy>,
    private val eventPublisher: DomainEventPublisher
) {

    fun execute(input: Input): Output {
        val application = repository.findById(ApplicationId.of(input.applicationId))
            .orElseThrow { CreditApplicationNotFoundException(input.applicationId) }

        application.startReview()
        eventPublisher.publishAll(application.pullDomainEvents())

        val score: CreditScore = scoreProvider.getScore(application.applicantDocument)

        val rejection = policies
            .map { it.evaluate(application, score) }
            .filterIsInstance<PolicyResult.Rejected>()
            .firstOrNull()

        if (rejection != null) {
            application.reject(score, rejection.reason)
        } else {
            application.approve(score)
        }

        repository.save(application)
        eventPublisher.publishAll(application.pullDomainEvents())

        return Output(
            applicationId = application.id.toString(),
            status = application.status,
            creditScore = score.value,
            riskClassification = score.classification.name,
            rejectionReason = application.rejectionReason
        )
    }

    data class Input(val applicationId: String)

    data class Output(
        val applicationId: String,
        val status: ApplicationStatus,
        val creditScore: Int,
        val riskClassification: String,
        val rejectionReason: String?
    )
}