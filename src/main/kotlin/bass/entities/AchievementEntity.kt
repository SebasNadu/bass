package bass.entities

import bass.enums.CouponType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.Instant
import java.time.temporal.ChronoUnit

@Entity
@Table(name = "achievement")
class AchievementEntity(
    @Column(nullable = false, unique = true)
    val name: String,
    @Column(name = "image_url")
    val imageUrl: String? = null,
    @Column(name = "streaks_required", nullable = false, unique = true)
    val streaksRequired: Int,
    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    val couponType: CouponType? = null,
    @Column(nullable = false)
    val description: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToMany(mappedBy = "achievements", fetch = FetchType.LAZY)
    val members: MutableSet<MemberEntity> = mutableSetOf(),
) : Auditable() {
    fun generatesCoupon(): Boolean = couponType != null

    fun generateCouponCode(memberId: Long): String {
        val timestamp = System.currentTimeMillis()
        return "${couponType?.name ?: "GENERIC"}_${memberId}_$timestamp"
    }

    fun createCouponFor(member: MemberEntity): CouponEntity {
        require(generatesCoupon()) { "Achievement $name does not generate coupons" }
        val code = generateCouponCode(member.id)
        val expiresAt = Instant.now().plus(couponType!!.validityDays, ChronoUnit.DAYS)

        return CouponEntity(
            code = code,
            member = member,
            achievement = this,
            couponType = couponType,
            expiresAt = expiresAt,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AchievementEntity) return false
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "AchievementEntity(id=$id, name='$name', strikesRequired=$streaksRequired, couponType=$couponType, " +
            "description='$description', imageUrl=$imageUrl)"
    }
}
