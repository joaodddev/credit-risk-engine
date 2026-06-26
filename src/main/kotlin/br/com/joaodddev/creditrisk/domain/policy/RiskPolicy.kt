package br.com.joaodddev.creditrisk.domain.policy

import br.com.joaodddev.creditrisk.domain.application.CreditApplication
import br.com.joaodddev.creditrisk.domain.application.CreditScore

interface RiskPolicy {
    fun evaluate(application: CreditApplication, score: CreditScore): PolicyResult
}