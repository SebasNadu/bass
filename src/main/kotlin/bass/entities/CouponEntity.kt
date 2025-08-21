package bass.entities

import bass.enums.CouponType
import bass.enums.DiscountType
import bass.exception.CouponAlreadyUsedException
import bass.exception.CouponExpiredException
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
        get() = couponType.discountAmount
    val discountType: DiscountType
        get() = couponType.discountType
    val discountValue: Int
        get() = couponType.discountValue
    val validityDays: Long
        get() = couponType.validityDays

    fun markAsUsed() {
        if (isUsed) {
            throw CouponAlreadyUsedException("Coupon with code=$code is already used")
        }
        if (isExpired()) {
            throw CouponExpiredException("Coupon with code=$code is expired")
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
}
