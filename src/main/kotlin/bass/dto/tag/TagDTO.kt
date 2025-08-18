package bass.dto.tag

data class TagDTO(
    val id: Long = 0L,
    val name: String,
)

data class CreateTagDTO(
    val name: String,
)
