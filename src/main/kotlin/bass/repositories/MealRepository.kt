package bass.repositories

import bass.entities.MealEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MealRepository : JpaRepository<MealEntity, Long> {
    fun existsByName(name: String): Boolean

    fun findByTagsName(tagName: String): List<MealEntity>
}
