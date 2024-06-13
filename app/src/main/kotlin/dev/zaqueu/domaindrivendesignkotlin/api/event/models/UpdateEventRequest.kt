package dev.zaqueu.domaindrivendesignkotlin.api.event.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.UpdatePartnerDto
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

@Serializable
internal data class UpdateEventRequest(
    val name: String,
    val description: String? = null,
    val date: String? = null,
) {
    fun toInput(id: String) = UpdateEventDto(
        id = id,
        name = name,
        description = description,
        date = date?.let { Instant.parse(it) }
    )
}
