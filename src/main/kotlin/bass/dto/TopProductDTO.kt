package bass.dto

import java.time.OffsetDateTime

// stays as OffsetDateTime because Instant causes a conflict with the result from native query

interface TopProductDTO {
    val name: String
    val count: Long
    val mostRecentAddedAt: OffsetDateTime
}
