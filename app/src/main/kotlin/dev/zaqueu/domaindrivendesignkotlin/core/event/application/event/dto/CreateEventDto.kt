package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import java.time.Instant

internal data class CreateEventDto(
    val name: String,
    val description: String?,
    val date: Instant,
    val partnerId: String,
)
