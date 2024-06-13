package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto

import java.time.Instant

internal data class UpdateEventDto(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val date: Instant? = null,
)
