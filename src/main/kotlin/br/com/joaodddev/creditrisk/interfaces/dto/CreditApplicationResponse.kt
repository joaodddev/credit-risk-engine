package br.com.joaodddev.creditrisk.interfaces.dto

import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreditApplicationResponse(
    val applicationId: String,
    val applicantDocument: String,
    val requestedAmount: BigDecimal,
    val status: ApplicationStatus,
    val creditScore: Int? = null,
    val riskClassification: String? = null,
    val rejectionReason: String? = null,
    val submittedAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)