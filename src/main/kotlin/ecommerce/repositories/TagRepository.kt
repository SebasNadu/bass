package ecommerce.repositories

import ecommerce.entities.TagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<TagEntity, Long> {
    fun findByName(name: TagEntity.TagNames): TagEntity?
}
