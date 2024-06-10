package dev.zaqueu.domaindrivendesignkotlin.api.order.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import kotlinx.serialization.Serializable

@Serializable
internal data class OrderResponse(
    val id: String,
    val amount: Long,
    val status: String,
    val customerId: String,
    val eventSpotId: String,
) {
    companion object {
        fun fromDomain(order: Order) = OrderResponse(
            id = order.id.value,
            amount = order.amount,
            status = order.status.name,
            customerId = order.customerId.value,
            eventSpotId = order.eventSpotId.value,
        )
    }
}
