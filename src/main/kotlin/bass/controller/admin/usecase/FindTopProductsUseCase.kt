package bass.controller.admin.usecase

import bass.dto.TopProductDTO

interface FindTopProductsUseCase {
    fun findProducts(): List<TopProductDTO>
}
