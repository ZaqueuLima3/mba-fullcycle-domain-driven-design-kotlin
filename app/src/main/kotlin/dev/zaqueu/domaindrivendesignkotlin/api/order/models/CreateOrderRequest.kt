package dev.zaqueu.domaindrivendesignkotlin.api.order.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.dto.CreateOrderDto
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateOrderRequest(
    val customerId: String,
    val spotId: String,
    val sectionId: String,
    val cardToken: String,
) {

    companion object {
        fun CreateOrderRequest.toInput(eventId: String) = CreateOrderDto(
            customerId = customerId,
            spotId = spotId,
            sectionId = sectionId,
            eventId = eventId,
            cardToken = cardToken,
        )
    }
}
