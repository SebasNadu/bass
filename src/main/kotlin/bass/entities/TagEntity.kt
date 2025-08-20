package bass.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "tag",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"])],
)
class TagEntity(
    @Column(nullable = false)
    val name: String,
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    val meals: MutableSet<MealEntity> = mutableSetOf(),
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    val members: MutableSet<MemberEntity> = mutableSetOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : Auditable()
