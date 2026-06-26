package br.com.joaodddev.creditrisk.application.usecase

import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import java.math.BigDecimal
import java.time.LocalDateTime

class ListApplicationsByDocumentUseCase(
    private val repository: CreditApplicationRepository
) {

    fun execute(input: Input): List<Output> {
        return repository.findByApplicantDocument(input.applicantDocument)
            .map { application ->
                Output(
                    applicationId = application.id.toString(),
                    requestedAmount = application.requestedAmount.value,
                    status = application.status,
                    creditScore = application.creditScore?.value,
                    riskClassification = application.creditScore?.classification?.name,
                    submittedAt = application.submittedAt
                )
            }
    }

    data class Input(val applicantDocument: String)

    data class Output(
        val applicationId: String,
        val requestedAmount: BigDecimal,
        val status: ApplicationStatus,
        val creditScore: Int?,
        val riskClassification: String?,
        val submittedAt: LocalDateTime
    )
}