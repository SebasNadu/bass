package bass.repositories

import bass.entities.DayEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DayRepository : JpaRepository<DayEntity, Long> {
    fun findByMemberId(memberId: Long): List<DayEntity>
}
