package br.com.joaodddev.creditrisk.interfaces.exception

import br.com.joaodddev.creditrisk.application.exception.CreditApplicationNotFoundException
import br.com.joaodddev.creditrisk.application.exception.InvalidApplicationStateException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CreditApplicationNotFoundException::class)
    fun handleNotFound(ex: CreditApplicationNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Not found").also {
            it.title = "Credit Application Not Found"
            it.type = URI.create("/errors/credit-application-not-found")
        }
    }

    @ExceptionHandler(InvalidApplicationStateException::class)
    fun handleInvalidState(ex: InvalidApplicationStateException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.message ?: "Invalid state").also {
            it.title = "Invalid Application State"
            it.type = URI.create("/errors/invalid-application-state")
        }
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid argument").also {
            it.title = "Invalid Argument"
            it.type = URI.create("/errors/invalid-argument")
        }
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(ex: IllegalStateException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.message ?: "Invalid operation").also {
            it.title = "Invalid Operation"
            it.type = URI.create("/errors/invalid-operation")
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ProblemDetail {
        val details = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details).also {
            it.title = "Validation Error"
            it.type = URI.create("/errors/validation")
        }
    }
}