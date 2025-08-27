package bass.services.streak

import bass.entities.MemberEntity
import bass.entities.OrderEntity
import bass.events.OrderCompletionEvent
import bass.repositories.AchievementRepository
import bass.repositories.MemberRepository
import bass.services.achievement.AchievementService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class StreakServiceImpl(
    private val achievementRepository: AchievementRepository,
    private val achievementService: AchievementService,
    private val memberRepository: MemberRepository,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleOrderCompletedEvent(event: OrderCompletionEvent) {
        val order = event.order
        val member = memberRepository.findById(order.member.id).orElseThrow()

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
        val member1 = memberRepository.findById(order.member.id).orElseThrow()
        memberRepository.save(member1)
    }

    fun updateStreak(
        order: OrderEntity,
        member: MemberEntity,
    ) {
        if (hasMajorityHealthyMeals(order)) {
            member.increaseStreak()
        } else if (!member.isFreedomDay()) {
            member.resetStreak()
        }
    }

    private fun hasMajorityHealthyMeals(order: OrderEntity): Boolean {
        val totalQuantity = order.items.sumOf { it.quantity }
        if (totalQuantity == 0) return false

        val healthyQuantity =
            order.items
                .filter { item -> item.meal.tags.any { tag -> tag.name == "Healthy" } }
                .sumOf { it.quantity }

        return healthyQuantity >= totalQuantity / 2.0
    }
}
