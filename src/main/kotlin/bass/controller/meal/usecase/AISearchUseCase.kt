package bass.controller.meal.usecase

import bass.dto.naturalSearch.NaturalSearchRequestDTO
import bass.dto.naturalSearch.NaturalSearchResponseDTO

interface AISearchUseCase {
    fun naturalSearch(request: NaturalSearchRequestDTO): NaturalSearchResponseDTO
}
