package ecommerce.controller.tag

import ecommerce.controller.tag.usecase.ManageTagUseCase
import ecommerce.dto.tag.TagDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(private val manageTagUseCase: ManageTagUseCase) {
    @GetMapping(TAGS)
    fun getTags(): ResponseEntity<List<TagDTO>> = ResponseEntity.ok(manageTagUseCase.findAll())

    companion object {
        const val TAGS = "/api/tags"
    }
}
