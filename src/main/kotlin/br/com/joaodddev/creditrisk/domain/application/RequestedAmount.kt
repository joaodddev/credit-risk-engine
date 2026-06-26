package br.com.joaodddev.creditrisk.domain.application

import java.math.BigDecimal

data class RequestedAmount(val value: BigDecimal) {

    init {
        require(value > BigDecimal.ZERO) { "Requested amount must be positive" }
        require(value <= MAX) { "Requested amount exceeds maximum allowed: $MAX" }
    }

    companion object {
        val MAX: BigDecimal = BigDecimal("500000.00")

        fun of(value: BigDecimal) = RequestedAmount(value)
        fun of(value: String) = RequestedAmount(BigDecimal(value))
    }

    override fun toString() = "R$ ${value.toPlainString()}"
}