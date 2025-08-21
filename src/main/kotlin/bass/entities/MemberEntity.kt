package bass.entities

import bass.exception.DayNameAlreadyExistsException
import bass.exception.DaysSizeAlreadyMaximumException
import bass.exception.InvalidTagNameException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class MemberEntity(
    @Column(name = "name", nullable = false, length = 50)
    val name: String,
    @Column(name = "email", nullable = false, length = 100)
    val email: String,
    @Column(name = "password", nullable = false, length = 255)
    val password: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    val role: Role = Role.CUSTOMER,
    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cartItems: MutableSet<CartItemEntity> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "member_tag",
        joinColumns = [JoinColumn(name = "member_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")],
    )
    val tags: MutableSet<TagEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    val coupons: List<CouponEntity> = mutableListOf(),
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "member_achievement",
        joinColumns = [JoinColumn(name = "member_id")],
        inverseJoinColumns = [JoinColumn(name = "achievement_id")],
    )
    val achievements: MutableSet<AchievementEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.REMOVE], orphanRemoval = true)
    val days: MutableSet<DayEntity> = mutableSetOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    init {
        require(days.size in DAYS_SIZE_MIN..DAYS_SIZE_MAX) { "A member must have 0 to 2 days assigned." }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MemberEntity) return false
        if (id == 0L || other.id == 0L) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    enum class Role { CUSTOMER, ADMIN }

    fun addTag(tag: TagEntity) {
        if (tags.any { it.name == tag.name }) {
            throw InvalidTagNameException("Tag with name '${tag.name}' already exists")
        }
        this.tags.add(tag)
        tag.members.add(this)
    }

    fun clearTags() {
        this.tags.clear()
    }

    fun addDay(day: DayEntity) {
        if (this.days.size == DAYS_SIZE_MAX) {
            throw DaysSizeAlreadyMaximumException("A member already has 2 days assigned.")
        }
        val dayNames = days.map { it.dayName.name }
        if (dayNames.contains(day.dayName.name)) {
            throw DayNameAlreadyExistsException("A member already has a day of ${day.dayName.name}!")
        }

        days.add(day)
        day.setMemberEntity(this)
    }

    fun removeDay(day: DayEntity) {
        assert(days.isNotEmpty()) { "A member does not have any day" }
        val dayNames = days.map { it.dayName.name }
        assert(dayNames.contains(day.dayName.name)) { "A member does not have a day of ${day.dayName.name}!" }

        days.remove(day)
        day.setMemberEntity(null)
    }

    companion object {
        const val DAYS_SIZE_MIN = 0
        const val DAYS_SIZE_MAX = 2
    }
}
