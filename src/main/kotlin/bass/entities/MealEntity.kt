package bass.entities

import bass.dto.MealDTO
import bass.dto.MealPatchDTO
import bass.exception.InsufficientStockException
import bass.exception.InvalidOptionNameException
import bass.exception.InvalidOptionQuantityException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "meal",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"])],
)
class MealEntity(
    name: String,
    quantity: Int,
    @Column(nullable = false)
    var price: Double,
    @OneToMany(mappedBy = "meal", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cartItems: MutableSet<CartItemEntity> = mutableSetOf(),
    @Column(nullable = false)
    var imageUrl: String,
//    @Column(nullable = false)
//    var description: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) : Auditable() {
    @Column(name = "name", nullable = false, length = 50)
    var name: String = name
        set(value) {
            validateName(value)
            field = value
        }

    @Column(name = "quantity", nullable = false)
    var quantity: Int = quantity
        set(value) {
            validateQuantity(value)
            field = value
        }

    init {
        this.name = name
        this.quantity = quantity
        // TODO: validate image URL
        // TODO: validate price
        // TODO: validate description
    }

    fun checkStock(quantity: Int) {
        if (this.quantity == 0) throw InsufficientStockException("Option is out of stock")
        if (this.quantity < quantity) throw InsufficientStockException("Not enough stock")
    }

    fun subtract(quantity: Int) {
        if (quantity < 1) throw InvalidOptionQuantityException("Subtract amount must be >= 1")
        if (this.quantity < quantity) throw InsufficientStockException("Not enough stock")
        this.quantity -= quantity
    }

    fun validateStock(quantity: Int) {
        if (this.quantity < quantity) throw InsufficientStockException("Not enough stock for option $id")
    }

    fun copyFrom(meal: MealDTO): MealEntity {
        this.name = meal.name
        this.quantity = meal.quantity
        this.imageUrl = meal.imageUrl
        this.price = meal.price
        return this
    }

    fun copyFrom(meal: MealPatchDTO): MealEntity {
        meal.name?.let { this.name = it }
        meal.quantity?.let { this.quantity = it }
        meal.price?.let { this.price = it }
        meal.imageUrl?.let { this.imageUrl = it }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other !is MealEntity) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        val result = id.hashCode()
        return result
    }

    private fun validateName(name: String) {
        if (name.length > 50) throw InvalidOptionNameException("Option name too long")
        val allowed = Regex("^[\\p{Alnum} ()\\[\\]+\\-&/_]+$")
        if (!allowed.matches(name)) throw InvalidOptionNameException("Option names contains invalid characters: '$name'")
    }

    private fun validateQuantity(quantity: Int) {
        if (quantity !in 1..<100_000_000) throw InvalidOptionQuantityException("Quantity must be between 1 and 99,999,999")
    }
}
