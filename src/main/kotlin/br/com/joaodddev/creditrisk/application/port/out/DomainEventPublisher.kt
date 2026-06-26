package br.com.joaodddev.creditrisk.application.port.out

import br.com.joaodddev.creditrisk.domain.event.DomainEvent

interface DomainEventPublisher {
    fun publishAll(events: List<DomainEvent>)
}