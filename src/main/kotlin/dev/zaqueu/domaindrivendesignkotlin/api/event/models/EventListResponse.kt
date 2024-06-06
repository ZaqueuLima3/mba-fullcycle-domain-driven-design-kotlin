package dev.zaqueu.domaindrivendesignkotlin.api.event.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import kotlinx.serialization.Serializable

@Serializable
internal data class EventListResponse(
    val id: String,
    val name: String,
    val description: String?,
    val date: String,
    val partnerId: String,
) {
    companion object {
        fun fromDomain(event: Event) = EventListResponse(
            id = event.id.value,
            name = event.name,
            description = event.description,
            date = event.date.toString(),
            partnerId = event.partnerId.value,
        )
    }
}
