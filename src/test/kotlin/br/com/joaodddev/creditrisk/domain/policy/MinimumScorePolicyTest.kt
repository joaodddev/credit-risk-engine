package br.com.joaodddev.creditrisk.domain.policy

import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import br.com.joaodddev.creditrisk.domain.application.RequestedAmount
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MinimumScorePolicyTest {

    private val policy = MinimumScorePolicy()
    private val application = CreditApplication.submit("123.456.789-00", RequestedAmount.of("10000.00"))

    @Test
    fun `should approve when score meets minimum threshold`() {
        val result = policy.evaluate(application, CreditScore.of(500))
        assertTrue(result.isApproved)
    }

    @Test
    fun `should reject when score is below minimum threshold`() {
        val result = policy.evaluate(application, CreditScore.of(300))
        assertFalse(result.isApproved)
        assertTrue(result is PolicyResult.Rejected)
    }
}