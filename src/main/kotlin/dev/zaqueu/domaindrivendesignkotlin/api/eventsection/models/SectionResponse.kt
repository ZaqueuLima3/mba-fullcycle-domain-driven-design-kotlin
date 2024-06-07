package dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models

import dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models.SpotResponse
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import kotlinx.serialization.Serializable

@Serializable
internal data class SectionResponse(
    val id: String,
    val name: String,
    val description: String?,
    val totalSpots: Long,
    val price: Long,
    val spots: List<SpotResponse>
) {
    companion object {
        fun fromDomain(section: EventSection) = SectionResponse(
            id = section.id.value,
            name = section.name,
            description = section.description,
            totalSpots = section.totalSpots,
            price = section.price,
            spots = section.spots.map { SpotResponse.fromDomain(it) }
        )
    }
}
