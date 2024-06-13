package dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventSectionDto
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateSectionRequest(
    val name: String? = null,
    val description: String? = null,
    val price: Long? = null,
) {
    fun toInput(sectionId: String, eventId: String) = UpdateEventSectionDto(
        sectionId = sectionId,
        eventId = eventId,
        name = name,
        description = description,
        price = price,
    )
}
