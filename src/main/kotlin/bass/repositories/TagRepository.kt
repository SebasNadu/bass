package bass.repositories

import bass.entities.TagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<TagEntity, Long> {
    fun findByName(name: String): TagEntity?
}
