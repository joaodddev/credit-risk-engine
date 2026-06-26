package br.com.joaodddev.creditrisk.domain.policy

import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import br.com.joaodddev.creditrisk.domain.application.RiskClassification
import br.com.joaodddev.creditrisk.domain.application.RequestedAmount
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class MaximumAmountPolicy : RiskPolicy {

    override fun evaluate(application: CreditApplication, score: CreditScore): PolicyResult {
        val limit = when (score.classification) {
            RiskClassification.VERY_LOW -> BigDecimal("500000.00")
            RiskClassification.LOW      -> BigDecimal("200000.00")
            RiskClassification.MEDIUM   -> BigDecimal("50000.00")
            RiskClassification.HIGH,
            RiskClassification.VERY_HIGH -> BigDecimal.ZERO
        }

        return if (application.requestedAmount.value <= limit) {
            PolicyResult.Approved
        } else {
            PolicyResult.Rejected(
                "Requested amount ${application.requestedAmount} exceeds limit of R$ ${limit.toPlainString()} for risk classification ${score.classification}"
            )
        }
    }
}