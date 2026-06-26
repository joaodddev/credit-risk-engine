package br.com.joaodddev.creditrisk.infrastructure.messaging

import br.com.joaodddev.creditrisk.application.port.out.DomainEventPublisher
import br.com.joaodddev.creditrisk.domain.event.DomainEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class DomainEventPublisherAdapter(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : DomainEventPublisher {

    private val log = LoggerFactory.getLogger(javaClass)
    private val topic = "credit.risk.events"

    override fun publishAll(events: List<DomainEvent>) {
        events.forEach { event ->
            kafkaTemplate.send(topic, event.eventId.toString(), event)
            log.info("Published event [{}] id={}", event.eventType, event.eventId)
        }
    }
}