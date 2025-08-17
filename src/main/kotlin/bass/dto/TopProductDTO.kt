package bass.dto

import java.time.LocalDateTime

interface TopProductDTO {
    val name: String
    val count: Long
    val mostRecentAddedAt: LocalDateTime
}
