package bass.dto.naturalSearch

import bass.dto.meal.MealResponseDTO
import bass.dto.tag.TagInferenceResultDTO
import bass.entities.MealEntity
import bass.mappers.toDTO

data class NaturalSearchResponseDTO(
    val selectedTags: List<String>,
    val meals: List<MealResponseDTO>,
) {
    companion object {
        fun from(
            tagInference: TagInferenceResultDTO,
            meals: List<MealEntity>,
        ): NaturalSearchResponseDTO {
            return NaturalSearchResponseDTO(
                selectedTags = tagInference.selectedTags,
                meals = meals.map { it.toDTO() },
            )
        }
    }
}
