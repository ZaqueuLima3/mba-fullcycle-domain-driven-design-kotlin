package dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import kotlinx.serialization.Serializable

@Serializable
internal data class SpotResponse(
    val id: String,
    val location: String?,
    val isPublished: Boolean,
    val isReserved: Boolean,
) {
    companion object {
        fun fromDomain(section: EventSpot) = SpotResponse(
            id = section.id.value,
            location = section.location,
            isPublished = section.isPublished,
            isReserved = section.isReserved,
        )
    }
}
