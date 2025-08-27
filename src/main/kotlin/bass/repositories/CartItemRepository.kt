package bass.repositories

import bass.dto.member.ActiveMemberDTO
import bass.dto.product.TopProductDTO
import bass.entities.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CartItemRepository : JpaRepository<CartItemEntity, Long> {
    fun findByMemberId(memberId: Long): List<CartItemEntity>

    fun existsByMealIdAndMemberId(
        mealId: Long,
        memberId: Long,
    ): Boolean

    fun findByMealIdAndMemberId(
        mealId: Long,
        memberId: Long,
    ): CartItemEntity?

    fun deleteByMealIdAndMemberId(
        mealId: Long,
        memberId: Long,
    )

    @Query(
        value = """
    SELECT m.name AS name,
           COUNT(*) AS count,
           MAX(c.added_at) AS mostRecentAddedAt
    FROM cart_item c
    JOIN meal m ON c.meal_id = m.id
    WHERE c.added_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
    GROUP BY c.meal_id, m.name
    ORDER BY count DESC, mostRecentAddedAt DESC
    LIMIT 5
  """,
        nativeQuery = true,
    )
    fun findTop5ProductsAddedInLast30Days(): List<TopProductDTO>

    @Query(
        nativeQuery = true,
        value = """
        SELECT DISTINCT m.id, m.name, m.email
        FROM cart_item c
        JOIN member m ON c.member_id = m.id
        WHERE c.added_at >= DATEADD('DAY', -7, CURRENT_TIMESTAMP)
    """,
    )
    fun findDistinctMembersWithCartActivityInLast7Days(): List<ActiveMemberDTO>
}
