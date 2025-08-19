package bass.controller.admin.usecase

import bass.dto.member.ActiveMemberDTO

interface FindMembersWithRecentCartActivityUseCase {
    fun findMembers(): List<ActiveMemberDTO>
}
