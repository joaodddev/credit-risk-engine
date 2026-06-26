package br.com.joaodddev.creditrisk.domain.policy

import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import org.springframework.stereotype.Component

@Component
class MinimumScorePolicy : RiskPolicy {

    override fun evaluate(application: CreditApplication, score: CreditScore): PolicyResult {
        return if (score.isEligible) {
            PolicyResult.Approved
        } else {
            PolicyResult.Rejected("Credit score ${score.value} is below the minimum threshold of 400")
        }
    }
}