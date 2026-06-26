package br.com.joaodddev.creditrisk.application.usecase

import br.com.joaodddev.creditrisk.application.port.out.DomainEventPublisher
import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditApplicationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SubmitCreditApplicationUseCaseTest {

    private val repository: CreditApplicationRepository = mock()
    private val eventPublisher: DomainEventPublisher = mock()
    private lateinit var useCase: SubmitCreditApplicationUseCase

    @BeforeEach
    fun setUp() {
        useCase = SubmitCreditApplicationUseCase(repository, eventPublisher)
    }

    @Test
    fun `should submit credit application successfully`() {
        whenever(repository.save(any())).thenAnswer { it.arguments[0] as CreditApplication }

        val output = useCase.execute(
            SubmitCreditApplicationUseCase.Input(
                applicantDocument = "123.456.789-00",
                requestedAmount = BigDecimal("15000.00")
            )
        )

        assertNotNull(output.applicationId)
        assertEquals(ApplicationStatus.PENDING, output.status)
        assertEquals("123.456.789-00", output.applicantDocument)
        verify(repository).save(any())
        verify(eventPublisher).publishAll(any())
    }
}