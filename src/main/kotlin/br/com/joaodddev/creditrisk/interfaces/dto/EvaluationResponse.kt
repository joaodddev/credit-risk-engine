package br.com.joaodddev.creditrisk.interfaces.dto

import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus

data class EvaluationResponse(
    val applicationId: String,
    val status: ApplicationStatus,
    val creditScore: Int,
    val riskClassification: String,
    val rejectionReason: String?
)