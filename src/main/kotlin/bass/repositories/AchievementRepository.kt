package bass.repositories

import bass.entities.AchievementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AchievementRepository : JpaRepository<AchievementEntity, Long> {
    @Query(
        """
      select a from MemberEntity m
      join m.achievements a
      where m.id = :memberId
    """,
    )
    fun findAllByMemberId(
        @Param("memberId") memberId: Long,
    ): List<AchievementEntity>

    @Query("SELECT a FROM AchievementEntity a JOIN a.members m WHERE m.id = :memberId")
    fun findByMemberId(
        @Param("memberId") memberId: Long,
    ): List<AchievementEntity>
}
