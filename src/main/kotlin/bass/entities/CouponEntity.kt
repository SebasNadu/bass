package bass.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.time.temporal.ChronoUnit

/*
doc about coupon creation:
- a DTO containing member and coupon name is received.
- fun createFrom determines the corresponding discountRate (the percentage to be discounted from the order)
using the name of the coupon as reference.
- expiresAt calculates the moment when the coupon was created plus 30 days.
- in service there is a function validateUsability which checks if the coupon is expiring after the current day.
*/

@Entity
@Table(name = "coupon")
class CouponEntity(
    @Column(nullable = false)
    val name: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_rate", nullable = false)
    val discountRate: DiscountRate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,
    @Column(name = "expires_at", nullable = false)
    val expiresAt: Instant = Instant.now().plus(30, ChronoUnit.DAYS),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : Auditable() {
    enum class DiscountRate(val value: Int) {
        FIVE_PERCENT(5),
        TEN_PERCENT(10),
        FIFTEEN_PERCENT(15),
        TWENTY_PERCENT(20),
    }

    companion object {
        fun createFrom(
            name: String,
            member: MemberEntity,
        ): CouponEntity {
            val discountRate =
                when (name.uppercase()) {
                    "FIRST_RANK" -> DiscountRate.FIVE_PERCENT
                    "SECOND_RANK" -> DiscountRate.TEN_PERCENT
                    "THIRD_RANK" -> DiscountRate.FIFTEEN_PERCENT
                    "FOURTH_RANK" -> DiscountRate.TWENTY_PERCENT
                    else -> throw IllegalArgumentException("Invalid or unknown coupon name: $name")
                }
            return CouponEntity(
                name = name,
                discountRate = discountRate,
                member = member,
            )
        }
    }
}
