package bass.controller.tag

import bass.controller.tag.usecase.ManageTagUseCase
import bass.dto.tag.CreateTagDTO
import bass.dto.tag.TagDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(private val manageTagUseCase: ManageTagUseCase) {
    @GetMapping(TAGS)
    fun getTags(): ResponseEntity<List<TagDTO>> = ResponseEntity.ok(manageTagUseCase.findAll())

    @PostMapping(TAGS)
    fun createTag(
        @RequestBody tag: CreateTagDTO,
    ): ResponseEntity<TagDTO> = ResponseEntity.ok(manageTagUseCase.create(tag))

    companion object {
        const val TAGS = "/api/tags"
    }
}
