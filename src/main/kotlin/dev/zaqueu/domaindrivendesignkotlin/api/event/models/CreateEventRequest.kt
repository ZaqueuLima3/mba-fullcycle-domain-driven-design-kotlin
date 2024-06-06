package dev.zaqueu.domaindrivendesignkotlin.api.event.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.CreateEventDto
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
internal data class CreateEventRequest(
    val name: String,
    val description: String?,
    val date: String,
    val partnerId: String,
) {
    fun toInput() = CreateEventDto(
        name = name,
        description = description,
        date = Instant.parse(date),
        partnerId = partnerId,
    )
}
