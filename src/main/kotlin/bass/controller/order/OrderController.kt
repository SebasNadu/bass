package bass.controller.order

import bass.annotation.LoginMember
import bass.controller.member.usecase.CrudMemberUseCase
import bass.controller.order.usecase.OrderCreationUseCase
import bass.dto.OrderCreationResponseDTO
import bass.dto.member.MemberLoginDTO
import bass.model.PaymentRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/order")
class OrderController(
    private val orderCreationUseCase: OrderCreationUseCase,
    private val crudMemberUseCase: CrudMemberUseCase,
) {
    @PostMapping
    fun createOrder(
        @LoginMember member: MemberLoginDTO,
        @RequestBody paymentRequest: PaymentRequest,
    ): ResponseEntity<OrderCreationResponseDTO> {
        val order = orderCreationUseCase.create(member, paymentRequest)
        val memberWithAchievement = crudMemberUseCase.findMemberNewAchievement(member.id)
        val response =
            OrderCreationResponseDTO(
                order = order,
                memberDetails = memberWithAchievement,
            )
        return ResponseEntity.ok(response)
    }
}
