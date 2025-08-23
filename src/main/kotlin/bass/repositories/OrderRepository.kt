package bass.repositories

import bass.entities.MealEntity
import bass.entities.OrderEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface OrderRepository : JpaRepository<OrderEntity, Long> {
    @Query(
        """
            SELECT i.meal FROM OrderEntity o
            JOIN o.items i
            WHERE o.createdAt >= :since
            AND o.status = 'PAID'
            GROUP BY i.meal
            ORDER BY COUNT(i.meal) DESC
           """,
    )
    fun findTopOrderedMealsSince(
        @Param("since") since: Instant,
        pageable: Pageable,
    ): List<MealEntity>

    @Query(
        """
            SELECT o FROM OrderEntity o
            WHERE o.member.id = :memberId
            ORDER BY o.createdAt DESC
            """,
    )
    fun findRecentOrdersByMember(
        @Param("memberId") memberId: Long,
        pageable: Pageable,
    ): List<OrderEntity>

    @Query(
        """
            SELECT i.meal FROM OrderEntity o
            JOIN o.items i
            JOIN i.meal.tags t 
            WHERE o.createdAt >= :since
            AND o.status = 'PAID'
            AND t.name IN :tagNames
            GROUP BY i.meal
            ORDER BY COUNT(i.meal) DESC
            """,
    )
    fun findTopOrderedMealsByTagSince(
        @Param("since") since: Instant,
        @Param("tagNames") tagNames: List<String>,
        pageable: Pageable,
    ): List<MealEntity>
}
