package br.com.joaodddev.creditrisk.application.usecase

import br.com.joaodddev.creditrisk.application.exception.CreditApplicationNotFoundException
import br.com.joaodddev.creditrisk.domain.application.ApplicationId
import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import java.math.BigDecimal
import java.time.LocalDateTime

class GetCreditApplicationUseCase(
    private val repository: CreditApplicationRepository
) {

    fun execute(input: Input): Output {
        val application = repository.findById(ApplicationId.of(input.applicationId))
            .orElseThrow { CreditApplicationNotFoundException(input.applicationId) }

        return Output(
            applicationId = application.id.toString(),
            applicantDocument = application.applicantDocument,
            requestedAmount = application.requestedAmount.value,
            status = application.status,
            creditScore = application.creditScore?.value,
            riskClassification = application.creditScore?.classification?.name,
            rejectionReason = application.rejectionReason,
            submittedAt = application.submittedAt,
            updatedAt = application.updatedAt
        )
    }

    data class Input(val applicationId: String)

    data class Output(
        val applicationId: String,
        val applicantDocument: String,
        val requestedAmount: BigDecimal,
        val status: ApplicationStatus,
        val creditScore: Int?,
        val riskClassification: String?,
        val rejectionReason: String?,
        val submittedAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )
}