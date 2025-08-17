package bass.controller.admin.usecase

import bass.dto.ActiveMemberDTO

interface FindMembersWithRecentCartActivityUseCase {
    fun findMembers(): List<ActiveMemberDTO>
}
