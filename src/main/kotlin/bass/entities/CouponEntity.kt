package bass.entities

import bass.enums.CouponType
import bass.enums.DiscountType
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
import java.math.BigDecimal
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

//@Entity
//@Table(name = "coupon")
//class CouponEntity(
//    @Column(nullable = false)
//    val name: String,
//    @Enumerated(EnumType.STRING)
//    @Column(name = "discount_rate", nullable = false)
//    val discountRate: DiscountRate,
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    val member: MemberEntity,
//    @Column(name = "expires_at", nullable = false)
//    val expiresAt: Instant = Instant.now().plus(30, ChronoUnit.DAYS),
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long = 0L,
//) : Auditable() {
//    enum class DiscountRate(val value: Int) {
//        FIVE_PERCENT(5),
//        TEN_PERCENT(10),
//        FIFTEEN_PERCENT(15),
//        TWENTY_PERCENT(20),
//    }
//
//    companion object {
//        fun createFrom(
//            name: String,
//            member: MemberEntity,
//        ): CouponEntity {
//            val discountRate =
//                when (name.uppercase()) {
//                    "FIRST_RANK" -> DiscountRate.FIVE_PERCENT
//                    "SECOND_RANK" -> DiscountRate.TEN_PERCENT
//                    "THIRD_RANK" -> DiscountRate.FIFTEEN_PERCENT
//                    "FOURTH_RANK" -> DiscountRate.TWENTY_PERCENT
//                    else -> throw IllegalArgumentException("Invalid or unknown coupon name: $name")
//                }
//            return CouponEntity(
//                name = name,
//                discountRate = discountRate,
//                member = member,
//            )
//        }
//    }
//}

@Entity
@Table(name = "coupon")
class CouponEntity(
    @Column(nullable = false, unique = true)
    val code: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    val achievement: AchievementEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type", nullable = false)
    val couponType: CouponType,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: Instant,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : Auditable() {

    @Column(name = "is_used", nullable = false)
    var isUsed: Boolean = false
        private set
    @Column(name = "used_at")
    var usedAt: Instant? = null
        private set

    val discountAmount: BigDecimal
        get() = couponType.getDiscountAmount()
    val discountType: DiscountType
        get() = couponType.discountType

    fun markAsUsed() {
        if (isUsed) {
            throw IllegalStateException("Coupon is already used")
        }
        this.isUsed = true
        this.usedAt = Instant.now()
    }
    fun isExpired(): Boolean = Instant.now().isAfter(expiresAt)
    fun isValid(): Boolean = !isUsed && !isExpired()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CouponEntity) return false
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "CouponEntity(id=$id, code='$code', type=$couponType, isUsed=$isUsed)"
    }

    companion object {
        fun createFromAchievement(
            member: MemberEntity,
            achievement: AchievementEntity
        ): CouponEntity? {
            val couponType = achievement.couponType ?: return null

            val code = achievement.generateCouponCode(member.id)
            val expiresAt = Instant.now().plus(couponType.validityDays.toLong(), ChronoUnit.DAYS)

            return CouponEntity(
                code = code,
                member = member,
                achievement = achievement,
                couponType = couponType,
                expiresAt = expiresAt
            )
        }
    }
}
