package br.com.joaodddev.creditrisk.interfaces.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class SubmitCreditApplicationRequest(
    @field:NotBlank(message = "Applicant document is required")
    val applicantDocument: String,

    @field:NotNull(message = "Requested amount is required")
    @field:Positive(message = "Requested amount must be positive")
    val requestedAmount: BigDecimal
)