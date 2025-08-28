package bass.mappers

import bass.dto.achievement.AchievementDTO
import bass.dto.coupon.CouponDTO
import bass.dto.member.MemberCouponDTO
import bass.dto.member.MemberLoginDTO
import bass.dto.member.MemberProfileDTO
import bass.dto.member.MemberRegisterDTO
import bass.entities.AchievementEntity
import bass.entities.CouponEntity
import bass.entities.DayEntity
import bass.entities.MemberEntity
import bass.entities.TagEntity
import bass.model.Member
import java.time.Instant
import java.time.temporal.ChronoUnit

fun MemberEntity.toDTO(): Member {
    val freedomDays = days.map { it.dayName.name }.toSet()
    return Member(
        name = this.name,
        email = this.email,
        password = this.password,
        role = this.role,
        id = this.id,
        testimonial = this.testimonial,
        tags = this.tags.map { it.toDTO() }.toSet(),
        freedomDays = freedomDays,
        streak = this.streak,
    )
}

fun MemberEntity.toLoginDTO() = MemberLoginDTO(id)

fun MemberEntity.toEntity() = MemberEntity(name, email, password, role, id = id)

fun MemberRegisterDTO.toEntity(
    tags: Set<TagEntity>,
    days: Set<DayEntity>,
): MemberEntity {
    return MemberEntity(
        name = this.name,
        email = this.email,
        password = this.password,
        testimonial = this.testimonial,
        tags = tags.toMutableSet(),
        days = days.toMutableSet(),
    )
}

fun MemberEntity.toOrderDTO(): MemberCouponDTO {
    val minutesAgo = Instant.now().minus(2, ChronoUnit.MINUTES)
    val recentCouponDTO =
        this.coupons
            .lastOrNull()
            ?.takeIf { it.createdAt.isAfter(minutesAgo) }
            ?.toOrderDTO()

    return MemberCouponDTO(
        memberId = this.id,
        newCoupon = recentCouponDTO,
    )
}

fun MemberEntity.toProfileDTO(
    achievements: List<AchievementEntity>,
    coupons: List<CouponEntity>,
): MemberProfileDTO {
    return MemberProfileDTO(
        name = this.name,
        email = this.email,
        streak = this.streak,
        achievements = achievements.map { AchievementDTO.fromEntity(it) },
        testimonial = this.testimonial,
        coupons = coupons.map { it.toDTO() },
        tags = this.tags.map { it.toDTO() },
        days = this.days.map { it.toDTO()},
    )
}
