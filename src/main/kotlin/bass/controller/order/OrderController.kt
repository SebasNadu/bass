package bass.controller.order

import bass.annotation.LoginMember
import bass.controller.order.usecase.OrderCreationUseCase
import bass.dto.MemberLoginDTO
import bass.dto.OrderDTO
import bass.model.PaymentRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/order")
class OrderController(private val orderCreationUseCase: OrderCreationUseCase) {
    @PostMapping
    fun createOrder(
        @LoginMember member: MemberLoginDTO,
        @RequestBody paymentRequest: PaymentRequest,
    ): ResponseEntity<OrderDTO> {
        val order = orderCreationUseCase.create(member, paymentRequest)
        return ResponseEntity.ok(order)
    }
}
