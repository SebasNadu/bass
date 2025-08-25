package bass.mappers

import bass.dto.day.DayDTO
import bass.entities.DayEntity

fun DayEntity.toDTO() = DayDTO(this.dayName.name)
