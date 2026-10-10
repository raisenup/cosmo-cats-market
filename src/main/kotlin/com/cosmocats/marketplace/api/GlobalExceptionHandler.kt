package com.cosmocats.marketplace.api

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant
import java.util.NoSuchElementException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetails> {
        val error = exception.bindingResult.fieldErrors.firstOrNull()
        val field = error?.field ?: "request"
        val message = error?.defaultMessage ?: "must be valid"
        return problem(
            type = "https://cosmo-cats.market/errors/validation",
            title = "Validation failed",
            status = HttpStatus.BAD_REQUEST,
            detail = "Field '$field' $message.",
            instance = request.requestURI
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(
        exception: NoSuchElementException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetails> = problem(
        type = "https://cosmo-cats.market/errors/not-found",
        title = "Resource not found",
        status = HttpStatus.NOT_FOUND,
        detail = exception.message ?: "The requested resource was not found.",
        instance = request.requestURI
    )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        exception: ConstraintViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetails> {
        val violation = exception.constraintViolations.firstOrNull()
        val field = violation?.propertyPath?.toString()?.substringAfterLast('.') ?: "request"
        return problem(
            type = "https://cosmo-cats.market/errors/validation",
            title = "Validation failed",
            status = HttpStatus.BAD_REQUEST,
            detail = "Field '$field' ${violation?.message ?: "must be valid"}.",
            instance = request.requestURI
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class, HttpMessageNotReadableException::class)
    fun handleMalformedRequest(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetails> = problem(
        type = "https://cosmo-cats.market/errors/validation",
        title = "Validation failed",
        status = HttpStatus.BAD_REQUEST,
        detail = "Request contains an invalid value or format.",
        instance = request.requestURI
    )

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        exception: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ProblemDetails> = problem(
        type = "https://cosmo-cats.market/errors/validation",
        title = "Validation failed",
        status = HttpStatus.BAD_REQUEST,
        detail = exception.message ?: "Request contains an invalid value.",
        instance = request.requestURI
    )

    private fun problem(
        type: String,
        title: String,
        status: HttpStatus,
        detail: String,
        instance: String
    ) = ResponseEntity.status(status)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(ProblemDetails(type, title, status.value(), detail, instance))
}

data class ProblemDetails(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String,
    val timestamp: Instant = Instant.now()
)
