package bass.dto.meal

import bass.dto.tag.TagDTO

data class MealResponseDTO(
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String,
    var description: String,
    var tags: Set<TagDTO>,
    var id: Long = 0L,
)
