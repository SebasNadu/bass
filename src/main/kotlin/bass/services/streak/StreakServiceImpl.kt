package bass.services.streak

import bass.entities.MemberEntity
import bass.entities.OrderEntity
import bass.events.OrderCompletionEvent
import bass.repositories.AchievementRepository
import bass.services.achievement.AchievementService
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class StreakServiceImpl(
    private val achievementRepository: AchievementRepository,
    private val achievementService: AchievementService,
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleOrderCompletedEvent(event: OrderCompletionEvent) {
        val order = event.order
        val member = order.member

        updateStreak(order, member)

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
