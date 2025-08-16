package ecommerce.entities

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
import java.time.LocalDateTime

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
    val expiresAt: LocalDateTime = LocalDateTime.now().plusDays(30),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : Auditable() {
    enum class DiscountRate(val value: Int) {
        FIVE_PERCENT(5),
        TEN_PERCENT(10),
        FIFTEEN_PERCENT(15),
        TWENTY_PERCENT(20);
    }
}
