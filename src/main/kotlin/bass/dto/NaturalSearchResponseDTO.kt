package bass.dto

import bass.entities.MealEntity

data class NaturalSearchResponseDTO(
    val selectedTags: List<String>,
    val meals: List<MealEntity>
) {
    companion object {
        fun from(tagInference: TagInferenceResultDTO, meals: List<MealEntity>): NaturalSearchResponseDTO {
            return NaturalSearchResponseDTO(
                selectedTags = tagInference.selectedTags,
                meals = meals
            )
        }
    }
}
