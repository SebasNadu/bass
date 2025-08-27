package bass.dto.achievement

import bass.entities.AchievementEntity

class AchievementDTO(
    val name: String,
    val imageUrl: String?,
) {
    companion object {
        fun fromEntity(entity: AchievementEntity): AchievementDTO {
            return AchievementDTO(
                entity.name,
                entity.imageUrl,
            )
        }
    }
}
