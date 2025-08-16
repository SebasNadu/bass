package ecommerce.advice

import ecommerce.dto.error.ErrorMessage
import ecommerce.dto.error.ErrorResponse
import ecommerce.exception.AuthorizationException
import ecommerce.exception.ForbiddenException
import ecommerce.exception.InsufficientStockException
import ecommerce.exception.InvalidCartItemQuantityException
import ecommerce.exception.InvalidOptionNameException
import ecommerce.exception.InvalidOptionQuantityException
import ecommerce.exception.NotFoundException
import ecommerce.exception.OperationFailedException
import ecommerce.exception.PaymentFailedException
import ecommerce.util.logger
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.ResourceAccessException
import java.time.Instant

@RestControllerAdvice(annotations = [RestController::class])
class ApiErrorControllerAdvice {
    private val log = logger<ApiErrorControllerAdvice>()

    private fun buildErrorResponse(
        status: HttpStatus,
        errorLabel: String,
        message: String,
        errors: List<ErrorMessage>? = null,
    ): ResponseEntity<ErrorResponse> {
        val body =
            ErrorResponse(
                status = status.value(),
                errorLabel = errorLabel,
                message = message,
                errors = errors,
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Not Found error"
        log.warn("NotFoundException occurred: $errorMessage", e)
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Operation failed", errorMessage)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotSuchElementException(e: NoSuchElementException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Not such element"
        log.warn("NoSuchElementException occurred: $errorMessage", e)
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Operation failed", errorMessage)
    }

    @ExceptionHandler(OperationFailedException::class)
    fun handleOperationFailedException(e: OperationFailedException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Operation failed"
        log.warn("OperationFailedException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Operation failed", errorMessage)
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(e: AuthorizationException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Authorization failed"
        log.warn("AuthorizationException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authorization failed", errorMessage)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(e: ForbiddenException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid credentials"
        log.warn("ForbiddenException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Authorization failed. Invalid Credentials", errorMessage)
    }

    @ExceptionHandler(InvalidCartItemQuantityException::class)
    fun handleInvalidCartItemQuantityException(e: InvalidCartItemQuantityException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid quantity"
        log.warn("InvalidCartItemQuantityException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid cart item quantity", errorMessage)
    }

    @ExceptionHandler(InvalidOptionNameException::class)
    fun handleInvalidOptionNameException(e: InvalidOptionNameException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid option name"
        log.warn("InvalidOptionNameException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid option name", errorMessage)
    }

    @ExceptionHandler(InvalidOptionQuantityException::class)
    fun handleInvalidOptionQuantityException(e: InvalidOptionQuantityException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid option quantity"
        log.warn("InvalidOptionQuantityException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid option quantity", errorMessage)
    }

    @ExceptionHandler(InsufficientStockException::class)
    fun handleInsufficientStockException(e: InsufficientStockException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Insufficient stock"
        log.warn("InsufficientStockException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.CONFLICT, "Insufficient stock", errorMessage)
    }

    @ExceptionHandler(PaymentFailedException::class)
    fun handlePaymentFailedException(e: PaymentFailedException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Payment failed"
        log.warn("PaymentFailedException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Payment failed", errorMessage)
    }

    @ExceptionHandler(ResourceAccessException::class)
    fun handleResourceAccessException(e: ResourceAccessException): ResponseEntity<ErrorResponse> {
        val errorMessage = "Request timed out"
        log.error("ResourceAccessException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.GATEWAY_TIMEOUT, "Request Timeout", errorMessage)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Generic error"
        log.error("Unhandled exception occurred: ${e.message}", e)
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", errorMessage)
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(e: DataAccessException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Data Access Error"
        log.error("DataAccessException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Empty result data access error", errorMessage)
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e: DuplicateKeyException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Duplicate key error"
        log.warn("DuplicateKeyException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.CONFLICT, "Duplicate Key Error", errorMessage)
    }

    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handleEmptyResultException(e: EmptyResultDataAccessException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Empty result for your query"
        log.warn("EmptyResultDataAccessException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Empty Result Data Access", errorMessage)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message
        log.warn("Validation failed: ${e.message}")

        val errors =
            e.bindingResult.fieldErrors.map {
                ErrorMessage(it.field, it.defaultMessage ?: "Validation error")
            }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errorMessage, errors = errors)
    }
}
