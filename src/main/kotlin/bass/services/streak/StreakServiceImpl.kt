package bass.services.streak

import bass.entities.MemberEntity
import bass.entities.OrderEntity
import bass.events.OrderCompletionEvent
import bass.repositories.AchievementRepository
import bass.repositories.MemberRepository
import bass.services.achievement.AchievementService
import jakarta.transaction.Transactional
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class StreakServiceImpl(
    private val memberRepository: MemberRepository,
    private val achievementRepository: AchievementRepository,
    private val achievementService: AchievementService,
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    @EventListener
    fun handleOrderCompletedEvent(event: OrderCompletionEvent) {
        val order = event.order
        val member = order.member

        updateStreak(order, member)
        memberRepository.save(member)

        if (member.streak > 0) {
            val achievements =
                achievementRepository.findAll()
                    .filter { it.streaksRequired == member.streak }
            achievements.forEach { achievement ->
                if (member.achievements.none { it.id == achievement.id }) {
                    achievementService.awardAchievement(member.id, achievement.id)
                }
            }
        }
        println("We just gave a coupon to member = ${member.name}")
    }

    fun updateStreak(
        order: OrderEntity,
        member: MemberEntity,
    ) {
        val hasHealthyMeal =
            order.items.any { item ->
                item.meal.tags.any { tag -> tag.name == "Healthy" }
            }
        if (hasHealthyMeal) {
            member.increaseStreak()
        } else if (!member.isFreedomDay()) {
            member.resetStreak()
        }
    }
}
