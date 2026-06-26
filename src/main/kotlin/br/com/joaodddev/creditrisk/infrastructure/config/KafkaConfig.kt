package br.com.joaodddev.creditrisk.infrastructure.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.TopicBuilder

@Configuration
@Profile("!test")
class KafkaConfig {

    @Bean
    fun creditRiskEventsTopic(): NewTopic = TopicBuilder
        .name("credit.risk.events")
        .partitions(3)
        .replicas(1)
        .build()
}