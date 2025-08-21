package bass.entities

import bass.dto.meal.MealPatchDTO
import bass.dto.meal.MealRequestDTO
import bass.exception.InsufficientStockException
import bass.exception.InvalidMealDescriptionException
import bass.exception.InvalidMealImageUrlException
import bass.exception.InvalidMealNameException
import bass.exception.InvalidMealPriceException
import bass.exception.InvalidMealQuantityException
import bass.exception.InvalidTagNameException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.math.BigDecimal

@Entity
@Table(
    name = "meal",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name"])],
)
class MealEntity(
    name: String,
    quantity: Int,
    price: BigDecimal,
    imageUrl: String,
    description: String,
    @OneToMany(mappedBy = "meal", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cartItems: MutableSet<CartItemEntity> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "tag_meal",
        joinColumns = [JoinColumn(name = "meal_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")],
    )
    var tags: MutableSet<TagEntity> = mutableSetOf(),
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

    @Column(name = "image_url", nullable = false)
    var imageUrl: String = imageUrl
        set(value) {
            validateImageUrl(value)
            field = value
        }

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    var price: BigDecimal = price
        set(value) {
            validatePrice(value)
            field = value
        }

    @Column(name = "description", nullable = false)
    var description: String = description
        set(value) {
            validateDescription(value)
            field = value
        }

    init {
        this.name = name
        this.quantity = quantity
        this.imageUrl = imageUrl
        this.price = price
        this.description = description
    }

    fun checkStock(quantity: Int) {
        if (this.quantity == 0) throw InsufficientStockException("Meal is out of stock")
        if (this.quantity < quantity) throw InsufficientStockException("Not enough stock")
    }

    fun subtract(quantity: Int) {
        if (quantity < 1) throw InvalidMealQuantityException("Subtract amount must be >= 1")
        if (this.quantity < quantity) throw InsufficientStockException("Not enough stock")
        this.quantity -= quantity
    }

    fun validateStock(quantity: Int) {
        if (this.quantity < quantity) throw InsufficientStockException("Not enough stock for meal $id")
    }

    fun copyFrom(meal: MealRequestDTO): MealEntity {
        this.name = meal.name
        this.quantity = meal.quantity
        this.imageUrl = meal.imageUrl
        this.price = meal.price
        this.description = meal.description
        return this
    }

    fun copyFrom(meal: MealPatchDTO): MealEntity {
        meal.name?.let { this.name = it }
        meal.quantity?.let { this.quantity = it }
        meal.price?.let { this.price = it }
        meal.imageUrl?.let { this.imageUrl = it }
        meal.description?.let { this.description = it }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MealEntity
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    private fun validateName(name: String) {
        if (name.length > 50) throw InvalidMealNameException("Meal name too long")
        val allowed = Regex("^[\\p{Alnum} ()\\[\\]+\\-&/_]+$")
        if (!allowed.matches(name)) throw InvalidMealNameException("Meal names contains invalid characters: '$name'")
    }

    private fun validateQuantity(quantity: Int) {
        if (quantity !in 1..<100_000_000) throw InvalidMealQuantityException("Quantity must be between 1 and 99,999,999")
    }

    private fun validateImageUrl(imageUrl: String) {
        if (!imageUrl.startsWith("http") && !imageUrl.startsWith("https")) {
            throw InvalidMealImageUrlException("ImageUrl must be http or https")
        }
    }

    private fun validatePrice(price: BigDecimal) {
        if (price <= BigDecimal.ZERO) throw InvalidMealPriceException("Price must be positive")
    }

    private fun validateDescription(description: String) {
        if (description.isEmpty()) throw InvalidMealDescriptionException("Provide a description")
    }

    fun addTag(tag: TagEntity) {
        if (tags.any { it.name == tag.name }) {
            throw InvalidTagNameException("Tag with name '${tag.name}' already exists")
        }
        this.tags.add(tag)
        tag.meals.add(this)
    }

    fun clearTags() {
        this.tags.clear()
    }
}
