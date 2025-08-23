package bass.repositories

import bass.entities.MealEntity
import bass.entities.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MealRepository : JpaRepository<MealEntity, Long> {
    fun existsByName(name: String): Boolean

    fun findByTagsName(tagName: String): List<MealEntity>

    @Query(
        """
            SELECT DISTINCT t FROM TagEntity t 
            JOIN t.meals m 
            JOIN m.cartItems ci
            WHERE ci.member.id =: memberId
            """,
    )
    fun findTagsByMemberId(
        @Param("memberId") memberId: Long,
    ): List<TagEntity>
}
