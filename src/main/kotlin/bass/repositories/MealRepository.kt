package bass.repositories

import bass.entities.MealEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MealRepository : JpaRepository<MealEntity, Long> {
    fun existsByName(name: String): Boolean

    fun findByTagsName(tagName: String): List<MealEntity>

    @Query(
        """
        select distinct m from MealEntity m
        join m.tags t
        where lower(t.name) in :tagNames
        """
    )
    fun findAnyTag(@Param("tagNames") tagNames: Collection<String>): List<MealEntity>

    @Query(
        """
        select m from MealEntity m
        join m.tags t
        where lower(t.name) in :tagNames
        group by m
        having count(distinct lower(t.name)) = :requiredCount
    """
    )
    fun findAllTags(
        @Param("tagNames") tagNames: Collection<String>,
        @Param("requiredCount") requiredCount: Long
    ): List<MealEntity>
}
