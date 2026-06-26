package br.com.joaodddev.creditrisk.domain.application

data class CreditScore(val value: Int) {

    init {
        require(value in 0..1000) { "Credit score must be between 0 and 1000" }
    }

    val classification: RiskClassification
        get() = when {
            value >= 800 -> RiskClassification.VERY_LOW
            value >= 600 -> RiskClassification.LOW
            value >= 400 -> RiskClassification.MEDIUM
            value >= 200 -> RiskClassification.HIGH
            else         -> RiskClassification.VERY_HIGH
        }

    val isEligible: Boolean
        get() = value >= 400

    companion object {
        fun of(value: Int) = CreditScore(value)
    }
}