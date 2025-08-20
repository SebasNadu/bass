package bass.repositories

import bass.entities.AchievementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AchievementRepository : JpaRepository<AchievementEntity, Long>
