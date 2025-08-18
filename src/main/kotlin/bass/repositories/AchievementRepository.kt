package bass.repositories

import bass.entities.Achievement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AchievementRepository : JpaRepository<Achievement, Long>
