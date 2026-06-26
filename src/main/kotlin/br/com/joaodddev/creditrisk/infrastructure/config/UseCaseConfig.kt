package br.com.joaodddev.creditrisk.infrastructure.config

import br.com.joaodddev.creditrisk.application.port.out.CreditScoreProvider
import br.com.joaodddev.creditrisk.application.port.out.DomainEventPublisher
import br.com.joaodddev.creditrisk.application.usecase.EvaluateCreditApplicationUseCase
import br.com.joaodddev.creditrisk.application.usecase.GetCreditApplicationUseCase
import br.com.joaodddev.creditrisk.application.usecase.ListApplicationsByDocumentUseCase
import br.com.joaodddev.creditrisk.application.usecase.SubmitCreditApplicationUseCase
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import br.com.joaodddev.creditrisk.domain.policy.MaximumAmountPolicy
import br.com.joaodddev.creditrisk.domain.policy.MinimumScorePolicy
import br.com.joaodddev.creditrisk.domain.policy.RiskPolicy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfig {

    @Bean
    fun riskPolicies(
        minimumScorePolicy: MinimumScorePolicy,
        maximumAmountPolicy: MaximumAmountPolicy
    ): List<RiskPolicy> = listOf(minimumScorePolicy, maximumAmountPolicy)

    @Bean
    fun submitCreditApplicationUseCase(
        repository: CreditApplicationRepository,
        eventPublisher: DomainEventPublisher
    ) = SubmitCreditApplicationUseCase(repository, eventPublisher)

    @Bean
    fun evaluateCreditApplicationUseCase(
        repository: CreditApplicationRepository,
        scoreProvider: CreditScoreProvider,
        policies: List<RiskPolicy>,
        eventPublisher: DomainEventPublisher
    ) = EvaluateCreditApplicationUseCase(repository, scoreProvider, policies, eventPublisher)

    @Bean
    fun getCreditApplicationUseCase(
        repository: CreditApplicationRepository
    ) = GetCreditApplicationUseCase(repository)

    @Bean
    fun listApplicationsByDocumentUseCase(
        repository: CreditApplicationRepository
    ) = ListApplicationsByDocumentUseCase(repository)
}