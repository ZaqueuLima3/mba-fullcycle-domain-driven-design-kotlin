package dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import kotlinx.serialization.Serializable

@Serializable
internal data class SectionListResponse(
    val id: String,
    val name: String,
    val description: String?,
    val totalSpots: Long,
    val price: Long,
) {
    companion object {
        fun fromDomain(section: EventSection) = SectionListResponse(
            id = section.id.value,
            name = section.name,
            description = section.description,
            totalSpots = section.totalSpots,
            price = section.price,
        )
    }
}
