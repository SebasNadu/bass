package bass.advice

import bass.dto.error.ErrorMessage
import bass.dto.error.ErrorResponse
import bass.exception.AuthorizationException
import bass.exception.CouponAlreadyUsedException
import bass.exception.CouponExpiredException
import bass.exception.DayNameAlreadyExistsException
import bass.exception.DaysSizeAlreadyMaximumException
import bass.exception.ForbiddenException
import bass.exception.InsufficientStockException
import bass.exception.InvalidCartItemQuantityException
import bass.exception.InvalidMealDescriptionException
import bass.exception.InvalidMealNameException
import bass.exception.InvalidMealQuantityException
import bass.exception.InvalidTagNameException
import bass.exception.MissingPreferredTagsException
import bass.exception.NoDaysSetException
import bass.exception.NoMealRecommendationException
import bass.exception.NotFoundException
import bass.exception.OperationFailedException
import bass.exception.PaymentFailedException
import bass.util.logger
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
import kotlin.NoSuchElementException

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
            ).apply {
                createdAt = Instant.now()
                updatedAt = Instant.now()
            }
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

    @ExceptionHandler(InvalidMealNameException::class)
    fun handleInvalidMealNameException(e: InvalidMealNameException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid Meal name"
        log.warn("InvalidMealNameException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Meal name", errorMessage)
    }

    @ExceptionHandler(InvalidMealQuantityException::class)
    fun handleInvalidMealQuantityException(e: InvalidMealQuantityException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid Meal quantity"
        log.warn("InvalidMealQuantityException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Meal quantity", errorMessage)
    }

    @ExceptionHandler(InvalidMealDescriptionException::class)
    fun handleInvalidMealDescriptionException(e: InvalidMealDescriptionException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid Meal description"
        log.warn("InvalidMealDescriptionException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Meal description", errorMessage)
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

    @ExceptionHandler(InvalidTagNameException::class)
    fun handleInvalidTagNameException(e: InvalidTagNameException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Invalid tag name"
        log.warn("InvalidTagNameException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid tag name", errorMessage)
    }

    @ExceptionHandler(CouponAlreadyUsedException::class)
    fun handleCouponAlreadyUsedException(e: CouponAlreadyUsedException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Coupon already used"
        log.warn("CouponAlreadyUsedException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.CONFLICT, "Coupon already used", errorMessage)
    }

    @ExceptionHandler(CouponExpiredException::class)
    fun handleCouponExpiredException(e: CouponExpiredException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Coupon expired"
        log.warn("CouponExpiredException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.GONE, "Coupon expired", errorMessage)
    }

    @ExceptionHandler(DaysSizeAlreadyMaximumException::class)
    fun handleDaysSizeAlreadyMaximumException(e: DaysSizeAlreadyMaximumException): ResponseEntity<ErrorResponse> {
        val defaultMessage = "Days size for member already at maximum"
        val errorMessage = e.message ?: defaultMessage
        log.warn("DaysSizeAlreadyMaximumException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.CONFLICT, defaultMessage, errorMessage)
    }

    @ExceptionHandler(DayNameAlreadyExistsException::class)
    fun handleDayNameAlreadyExistsException(e: DayNameAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val defaultMessage = "Day name already exists"
        val errorMessage = e.message ?: defaultMessage
        log.warn("DayNameAlreadyExistsException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.CONFLICT, defaultMessage, errorMessage)
    }

    @ExceptionHandler(MissingPreferredTagsException::class)
    fun handleMissingPreferredTagsException(e: MissingPreferredTagsException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Member has no preferred tags"
        log.warn("MissingPreferredTagsException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Missing preferred tags", errorMessage)
    }

    @ExceptionHandler(NoMealRecommendationException::class)
    fun handleNoMealRecommendationException(e: NoMealRecommendationException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Member has no meal recommendation"
        log.warn("NoMealRecommendationException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.NOT_FOUND, "No meal recommendation", errorMessage)
    }

    @ExceptionHandler(NoDaysSetException::class)
    fun handleNoDaysSetException(e: NoDaysSetException): ResponseEntity<ErrorResponse> {
        val errorMessage = e.message ?: "Member has no days set"
        log.warn("NoDaysSetException: $errorMessage", e)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "No days set", errorMessage)
    }
}
