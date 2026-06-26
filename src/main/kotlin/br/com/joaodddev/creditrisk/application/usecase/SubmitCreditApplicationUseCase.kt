package br.com.joaodddev.creditrisk.application.usecase

import br.com.joaodddev.creditrisk.application.port.out.DomainEventPublisher
import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import br.com.joaodddev.creditrisk.domain.application.RequestedAmount
import java.math.BigDecimal

class SubmitCreditApplicationUseCase(
    private val repository: CreditApplicationRepository,
    private val eventPublisher: DomainEventPublisher
) {

    fun execute(input: Input): Output {
        val application = CreditApplication.submit(
            applicantDocument = input.applicantDocument,
            requestedAmount = RequestedAmount.of(input.requestedAmount)
        )

        repository.save(application)
        eventPublisher.publishAll(application.pullDomainEvents())

        return Output(
            applicationId = application.id.toString(),
            applicantDocument = application.applicantDocument,
            requestedAmount = application.requestedAmount.value,
            status = application.status
        )
    }

    data class Input(
        val applicantDocument: String,
        val requestedAmount: BigDecimal
    )

    data class Output(
        val applicationId: String,
        val applicantDocument: String,
        val requestedAmount: BigDecimal,
        val status: ApplicationStatus
    )
}