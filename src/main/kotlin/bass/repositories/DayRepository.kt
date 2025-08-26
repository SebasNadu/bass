package bass.repositories

import bass.entities.DayEntity
import bass.entities.TagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DayRepository : JpaRepository<DayEntity, Long> {
    fun findAllByMemberId(id: Long): List<DayEntity>
}
