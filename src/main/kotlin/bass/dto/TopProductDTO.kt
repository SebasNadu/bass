package bass.dto

import java.time.LocalDateTime

// for now it stays as LocalDateTime, because Instant causes a conflict with the result from native query
// maybe later it would be necessary to change to OffsetDateTime

interface TopProductDTO {
    val name: String
    val count: Long
    val mostRecentAddedAt: LocalDateTime
}
