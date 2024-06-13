package dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.CreateEventSectionDto
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateSectionRequest(
    val name: String,
    val description: String?,
    val totalSpots: Long,
    val price: Long,
) {
    fun toInput(eventId: String) = CreateEventSectionDto(
        eventId = eventId,
        name = name,
        description = description,
        totalSpots = totalSpots,
        price = price,
    )
}
