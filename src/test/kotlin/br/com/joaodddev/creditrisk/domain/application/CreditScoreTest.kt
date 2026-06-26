package br.com.joaodddev.creditrisk.domain.application

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreditScoreTest {

    @Test
    fun `should classify score above 800 as VERY_LOW risk`() {
        assertEquals(RiskClassification.VERY_LOW, CreditScore.of(850).classification)
    }

    @Test
    fun `should classify score between 400 and 599 as MEDIUM risk`() {
        assertEquals(RiskClassification.MEDIUM, CreditScore.of(500).classification)
    }

    @Test
    fun `should be eligible when score is 400 or above`() {
        assertTrue(CreditScore.of(400).isEligible)
    }

    @Test
    fun `should not be eligible when score is below 400`() {
        assertFalse(CreditScore.of(399).isEligible)
    }

    @Test
    fun `should throw when score is out of range`() {
        assertThrows<IllegalArgumentException> { CreditScore.of(1001) }
        assertThrows<IllegalArgumentException> { CreditScore.of(-1) }
    }
}