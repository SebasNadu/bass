package bass.enums

import java.math.BigDecimal

enum class CouponType(
    val displayName: String,
    val discountValue: Int,
    val discountType: DiscountType,
    val validityDays: Long = 30,
) {
    FIRST_RANK("First Rank Reward", 5, DiscountType.PERCENTAGE, 30),
    SECOND_RANK("Second Rank Reward", 10, DiscountType.PERCENTAGE, 45),
    THIRD_RANK("Third Rank Reward", 15, DiscountType.PERCENTAGE, 60),
    FOURTH_RANK("Fourth Rank Reward", 20, DiscountType.PERCENTAGE, 90),
    WELCOME_BONUS("Welcome Bonus", 5, DiscountType.FIXED_AMOUNT, 7), // $5.00
    FREE_SHIPPING("Free Shipping", 0, DiscountType.FREE_SHIPPING, 14),
    ;

    val discountAmount
        get(): BigDecimal {
            return when (discountType) {
                DiscountType.PERCENTAGE -> BigDecimal(discountValue)
                DiscountType.FIXED_AMOUNT -> BigDecimal(discountValue).divide(BigDecimal(100)) // cents to dollars
                DiscountType.FREE_SHIPPING -> BigDecimal.ZERO
            }
        }
}
