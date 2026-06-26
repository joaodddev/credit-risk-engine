package br.com.joaodddev.creditrisk.application.port.out

import br.com.joaodddev.creditrisk.domain.application.CreditScore

interface CreditScoreProvider {
    fun getScore(applicantDocument: String): CreditScore
}