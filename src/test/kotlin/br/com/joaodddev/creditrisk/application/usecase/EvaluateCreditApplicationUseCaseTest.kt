package br.com.joaodddev.creditrisk.application.usecase

import br.com.joaodddev.creditrisk.application.exception.CreditApplicationNotFoundException
import br.com.joaodddev.creditrisk.application.port.out.CreditScoreProvider
import br.com.joaodddev.creditrisk.application.port.out.DomainEventPublisher
import br.com.joaodddev.creditrisk.domain.application.*
import br.com.joaodddev.creditrisk.domain.policy.MinimumScorePolicy
import br.com.joaodddev.creditrisk.domain.policy.MaximumAmountPolicy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class EvaluateCreditApplicationUseCaseTest {

    private val repository: CreditApplicationRepository = mock()
    private val scoreProvider: CreditScoreProvider = mock()
    private val eventPublisher: DomainEventPublisher = mock()
    private lateinit var useCase: EvaluateCreditApplicationUseCase

    @BeforeEach
    fun setUp() {
        useCase = EvaluateCreditApplicationUseCase(
            repository = repository,
            scoreProvider = scoreProvider,
            policies = listOf(MinimumScorePolicy(), MaximumAmountPolicy()),
            eventPublisher = eventPublisher
        )
    }

    @Test
    fun `should approve application when score meets all policies`() {
        val application = CreditApplication.submit("123.456.789-00", RequestedAmount.of("10000.00"))
        whenever(repository.findById(any())).thenReturn(Optional.of(application))
        whenever(repository.save(any())).thenAnswer { it.arguments[0] as CreditApplication }
        whenever(scoreProvider.getScore(any())).thenReturn(CreditScore.of(750))

        val output = useCase.execute(EvaluateCreditApplicationUseCase.Input(application.id.toString()))

        assertEquals(ApplicationStatus.APPROVED, output.status)
        assertEquals(750, output.creditScore)
    }

    @Test
    fun `should reject application when score is below minimum`() {
        val application = CreditApplication.submit("123.456.789-00", RequestedAmount.of("10000.00"))
        whenever(repository.findById(any())).thenReturn(Optional.of(application))
        whenever(repository.save(any())).thenAnswer { it.arguments[0] as CreditApplication }
        whenever(scoreProvider.getScore(any())).thenReturn(CreditScore.of(300))

        val output = useCase.execute(EvaluateCreditApplicationUseCase.Input(application.id.toString()))

        assertEquals(ApplicationStatus.REJECTED, output.status)
        assertNotNull(output.rejectionReason)
    }

    @Test
    fun `should throw when application not found`() {
        whenever(repository.findById(any())).thenReturn(Optional.empty())

        assertThrows<CreditApplicationNotFoundException> {
            useCase.execute(EvaluateCreditApplicationUseCase.Input("00000000-0000-0000-0000-000000000000"))
        }
    }
}