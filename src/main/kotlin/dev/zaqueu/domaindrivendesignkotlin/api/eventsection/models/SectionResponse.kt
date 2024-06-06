package dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import kotlinx.serialization.Serializable

@Serializable
internal data class EventSectionResponse(
    val id: String,
    val name: String,
    val description: String?,
    val date: String,
    val partnerId: String,
    val sections: List<Section>
) {
    @Serializable
    data class Section(
        val id: String,
        val name: String,
        val description: String?,
        val totalSpots: Long,
        val price: Long,
    ) {
        companion object {
            fun fromDomain(section: EventSection) = Section(
                id = section.id.value,
                name = section.name,
                description = section.description,
                totalSpots = section.totalSpots,
                price = section.price,
            )
        }
    }

    companion object {
        fun fromDomain(event: Event) = EventSectionResponse(
            id = event.id.value,
            name = event.name,
            description = event.description,
            date = event.date.toString(),
            partnerId = event.partnerId.value,
            sections = event.sections.map { Section.fromDomain(it) }
        )
    }
}
