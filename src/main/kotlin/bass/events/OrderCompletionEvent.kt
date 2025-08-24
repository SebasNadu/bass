package bass.events

import bass.entities.OrderEntity
import org.springframework.context.ApplicationEvent

class OrderCompletionEvent(
    source: Any,
    val order: OrderEntity,
) : ApplicationEvent(source)
