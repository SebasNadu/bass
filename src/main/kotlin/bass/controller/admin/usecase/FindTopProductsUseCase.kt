package bass.controller.admin.usecase

import bass.dto.product.TopProductDTO

interface FindTopProductsUseCase {
    fun findProducts(): List<TopProductDTO>
}
