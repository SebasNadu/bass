package bass.entities

import bass.model.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "achievement")
class Achievement(
    @Column(nullable = false)
    val name: String,
    @Column(name = "image_url")
    val imageUrl: String? = null,

//    @Enumerated(EnumType.STRING)
//    @Column(name = "coupon_type")
//    val couponType: CouponType? = null,

    // Many-to-many relationship with members
    @ManyToMany(mappedBy = "achievements", fetch = FetchType.LAZY)
    val members: MutableSet<Achievement> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {
    // Override equals and hashCode for proper JPA behavior
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Achievement) return false
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
