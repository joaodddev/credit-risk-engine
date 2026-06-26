package br.com.joaodddev.creditrisk.domain.application.event

import br.com.joaodddev.creditrisk.domain.application.ApplicationId
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import br.com.joaodddev.creditrisk.domain.event.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

data class CreditApplicationRejectedEvent(
    override val eventId: UUID = UUID.randomUUID(),
    override val occurredAt: LocalDateTime = LocalDateTime.now(),
    val applicationId: ApplicationId,
    val creditScore: CreditScore,
    val reason: String
) : DomainEvent {
    override val eventType = "credit.application.rejected"
}