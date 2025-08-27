package bass.dto.product

import java.time.OffsetDateTime

interface TopProductDTO {
    val name: String
    val count: Long
    val mostRecentAddedAt: OffsetDateTime
}