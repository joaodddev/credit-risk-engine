package br.com.joaodddev.creditrisk.domain.application

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CreditApplicationTest {

    private val validAmount = RequestedAmount.of("10000.00")

    @Test
    fun `should submit application with PENDING status`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        assertEquals(ApplicationStatus.PENDING, application.status)
        assertNotNull(application.id)
    }

    @Test
    fun `should register submitted domain event on creation`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        val events = application.pullDomainEvents()
        assertEquals(1, events.size)
        assertEquals("credit.application.submitted", events.first().eventType)
    }

    @Test
    fun `should transition to UNDER_REVIEW from PENDING`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        application.pullDomainEvents()
        application.startReview()
        assertEquals(ApplicationStatus.UNDER_REVIEW, application.status)
    }

    @Test
    fun `should approve application under review`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        application.pullDomainEvents()
        application.startReview()
        application.pullDomainEvents()
        application.approve(CreditScore.of(750))
        assertEquals(ApplicationStatus.APPROVED, application.status)
        assertEquals(750, application.creditScore?.value)
    }

    @Test
    fun `should reject application under review`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        application.pullDomainEvents()
        application.startReview()
        application.pullDomainEvents()
        application.reject(CreditScore.of(200), "Score below minimum threshold")
        assertEquals(ApplicationStatus.REJECTED, application.status)
        assertNotNull(application.rejectionReason)
    }

    @Test
    fun `should throw on invalid status transition`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        assertThrows<IllegalArgumentException> { application.approve(CreditScore.of(800)) }
    }

    @Test
    fun `should clear domain events after pull`() {
        val application = CreditApplication.submit("123.456.789-00", validAmount)
        application.pullDomainEvents()
        assertTrue(application.pullDomainEvents().isEmpty())
    }
}