package bass.controller.meal.usecase

import bass.dto.NaturalSearchRequestDTO
import bass.dto.NaturalSearchResponseDTO

interface AISearchUseCase {
    fun naturalSearch(request: NaturalSearchRequestDTO): NaturalSearchResponseDTO
}
