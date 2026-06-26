package br.com.joaodddev.creditrisk.interfaces.controller

import br.com.joaodddev.creditrisk.application.usecase.EvaluateCreditApplicationUseCase
import br.com.joaodddev.creditrisk.application.usecase.GetCreditApplicationUseCase
import br.com.joaodddev.creditrisk.application.usecase.ListApplicationsByDocumentUseCase
import br.com.joaodddev.creditrisk.application.usecase.SubmitCreditApplicationUseCase
import br.com.joaodddev.creditrisk.domain.application.ApplicationStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@WebMvcTest(CreditApplicationController::class)
class CreditApplicationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var submitUseCase: SubmitCreditApplicationUseCase

    @MockBean
    private lateinit var evaluateUseCase: EvaluateCreditApplicationUseCase

    @MockBean
    private lateinit var getUseCase: GetCreditApplicationUseCase

    @MockBean
    private lateinit var listUseCase: ListApplicationsByDocumentUseCase

    @Test
    fun `should submit credit application and return 201`() {
        whenever(submitUseCase.execute(any())).thenReturn(
            SubmitCreditApplicationUseCase.Output(
                applicationId = "abc-123",
                applicantDocument = "123.456.789-00",
                requestedAmount = BigDecimal("10000.00"),
                status = ApplicationStatus.PENDING
            )
        )

        val body = mapOf(
            "applicantDocument" to "123.456.789-00",
            "requestedAmount" to "10000.00"
        )

        mockMvc.perform(
            post("/api/v1/credit-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.applicationId").value("abc-123"))
            .andExpect(jsonPath("$.status").value("PENDING"))
    }

    @Test
    fun `should return 400 when applicant document is blank`() {
        val body = mapOf(
            "applicantDocument" to "",
            "requestedAmount" to "10000.00"
        )

        mockMvc.perform(
            post("/api/v1/credit-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.title").value("Validation Error"))
    }

    @Test
    fun `should evaluate application and return decision`() {
        whenever(evaluateUseCase.execute(any())).thenReturn(
            EvaluateCreditApplicationUseCase.Output(
                applicationId = "abc-123",
                status = ApplicationStatus.APPROVED,
                creditScore = 750,
                riskClassification = "LOW",
                rejectionReason = null
            )
        )

        mockMvc.perform(
            post("/api/v1/credit-applications/abc-123/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("APPROVED"))
            .andExpect(jsonPath("$.creditScore").value(750))
            .andExpect(jsonPath("$.riskClassification").value("LOW"))
    }
}