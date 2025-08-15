package ecommerce.repositories

import ecommerce.entities.MealEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MealRepository : JpaRepository<MealEntity, Long> {
    fun existsByName(name: String): Boolean
}
