package bass.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "tag",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"])],
)
class TagEntity(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val name: TagNames,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : Auditable() {
    enum class TagNames {
        HEALTHY,
        NON_HEALTHY,
    }
}
