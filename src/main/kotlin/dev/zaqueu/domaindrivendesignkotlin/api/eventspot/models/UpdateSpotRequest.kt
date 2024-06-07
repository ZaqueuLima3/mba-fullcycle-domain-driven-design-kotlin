package dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventSpotLocationDto
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateSpotRequest(
    val location: String,
) {
    fun toInput(eventId: String, sectionId: String, spotId: String) = UpdateEventSpotLocationDto(
        eventId = eventId,
        sectionId = sectionId,
        spotId = spotId,
        location = location,
    )
}
