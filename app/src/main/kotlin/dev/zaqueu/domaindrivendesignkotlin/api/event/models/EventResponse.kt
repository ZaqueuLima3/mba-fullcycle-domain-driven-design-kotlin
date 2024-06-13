package dev.zaqueu.domaindrivendesignkotlin.api.event.models

import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.SectionResponse
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import kotlinx.serialization.Serializable

@Serializable
internal data class EventResponse(
    val id: String,
    val name: String,
    val description: String?,
    val date: String,
    val partnerId: String,
    val sections: List<SectionResponse>
) {
    companion object {
        fun fromDomain(event: Event) = EventResponse(
            id = event.id.value,
            name = event.name,
            description = event.description,
            date = event.date.toString(),
            partnerId = event.partnerId.value,
            sections = event.sections.map { SectionResponse.fromDomain(it) }
        )
    }
}
