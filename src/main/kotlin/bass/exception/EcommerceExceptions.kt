package bass.exception

class NotFoundException(message: String? = null) : RuntimeException(message)

class OperationFailedException(message: String? = null) : RuntimeException(message)

class AuthorizationException(message: String? = null) : RuntimeException(message)

class ForbiddenException(message: String? = null) : RuntimeException(message)

class InvalidCartItemQuantityException(quantity: Int) :
    RuntimeException("Quantity must be non-negative, got $quantity")

class NoSuchElementException(message: String? = null) : RuntimeException(message)

class InvalidMealNameException(message: String? = null) : RuntimeException(message)

class InvalidMealQuantityException(message: String? = null) : RuntimeException(message)

class InvalidMealPriceException(message: String? = null) : RuntimeException(message)

class InvalidMealDescriptionException(message: String? = null) : RuntimeException(message)

class InvalidMealImageUrlException(message: String? = null) : RuntimeException(message)

class InsufficientStockException(message: String? = null) : RuntimeException(message)

class PaymentFailedException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class InvalidTagNameException(message: String? = null) : RuntimeException(message)

class CouponAlreadyUsedException(message: String? = null) : RuntimeException(message)

class CouponExpiredException(message: String? = null) : RuntimeException(message)

class DaysSizeAlreadyMaximumException(message: String? = null) : RuntimeException(message)

class DayNameAlreadyExistsException(message: String? = null) : RuntimeException(message)

class MissingPreferredTagsException(message: String? = null) : RuntimeException(message)

class NoMealRecommendationException(message: String? = null) : RuntimeException(message)

class NoDaysSetException(message: String? = null) : RuntimeException(message)
