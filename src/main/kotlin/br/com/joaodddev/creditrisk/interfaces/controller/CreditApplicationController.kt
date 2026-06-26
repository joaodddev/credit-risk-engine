package br.com.joaodddev.creditrisk.interfaces.controller

import br.com.joaodddev.creditrisk.application.usecase.EvaluateCreditApplicationUseCase
import br.com.joaodddev.creditrisk.application.usecase.GetCreditApplicationUseCase
import br.com.joaodddev.creditrisk.application.usecase.ListApplicationsByDocumentUseCase
import br.com.joaodddev.creditrisk.application.usecase.SubmitCreditApplicationUseCase
import br.com.joaodddev.creditrisk.interfaces.dto.CreditApplicationResponse
import br.com.joaodddev.creditrisk.interfaces.dto.EvaluationResponse
import br.com.joaodddev.creditrisk.interfaces.dto.SubmitCreditApplicationRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/credit-applications")
class CreditApplicationController(
    private val submitUseCase: SubmitCreditApplicationUseCase,
    private val evaluateUseCase: EvaluateCreditApplicationUseCase,
    private val getUseCase: GetCreditApplicationUseCase,
    private val listUseCase: ListApplicationsByDocumentUseCase
) {

    @PostMapping
    fun submit(
        @Valid @RequestBody request: SubmitCreditApplicationRequest
    ): ResponseEntity<CreditApplicationResponse> {
        val output = submitUseCase.execute(
            SubmitCreditApplicationUseCase.Input(
                applicantDocument = request.applicantDocument,
                requestedAmount = request.requestedAmount
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreditApplicationResponse(
                applicationId = output.applicationId,
                applicantDocument = output.applicantDocument,
                requestedAmount = output.requestedAmount,
                status = output.status
            )
        )
    }

    @PostMapping("/{applicationId}/evaluate")
    fun evaluate(
        @PathVariable applicationId: String
    ): ResponseEntity<EvaluationResponse> {
        val output = evaluateUseCase.execute(
            EvaluateCreditApplicationUseCase.Input(applicationId)
        )
        return ResponseEntity.ok(
            EvaluationResponse(
                applicationId = output.applicationId,
                status = output.status,
                creditScore = output.creditScore,
                riskClassification = output.riskClassification,
                rejectionReason = output.rejectionReason
            )
        )
    }

    @GetMapping("/{applicationId}")
    fun getById(
        @PathVariable applicationId: String
    ): ResponseEntity<CreditApplicationResponse> {
        val output = getUseCase.execute(GetCreditApplicationUseCase.Input(applicationId))
        return ResponseEntity.ok(
            CreditApplicationResponse(
                applicationId = output.applicationId,
                applicantDocument = output.applicantDocument,
                requestedAmount = output.requestedAmount,
                status = output.status,
                creditScore = output.creditScore,
                riskClassification = output.riskClassification,
                rejectionReason = output.rejectionReason,
                submittedAt = output.submittedAt,
                updatedAt = output.updatedAt
            )
        )
    }

    @GetMapping
    fun listByDocument(
        @RequestParam applicantDocument: String
    ): ResponseEntity<List<CreditApplicationResponse>> {
        val outputs = listUseCase.execute(
            ListApplicationsByDocumentUseCase.Input(applicantDocument)
        )
        return ResponseEntity.ok(
            outputs.map {
                CreditApplicationResponse(
                    applicationId = it.applicationId,
                    applicantDocument = applicantDocument,
                    requestedAmount = it.requestedAmount,
                    status = it.status,
                    creditScore = it.creditScore,
                    riskClassification = it.riskClassification,
                    submittedAt = it.submittedAt
                )
            }
        )
    }
}