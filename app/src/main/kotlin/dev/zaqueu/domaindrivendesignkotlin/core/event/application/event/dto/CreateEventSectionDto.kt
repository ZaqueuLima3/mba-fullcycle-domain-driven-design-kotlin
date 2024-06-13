package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto

internal data class CreateEventSectionDto(
    val eventId: String,
    val name: String,
    val description: String?,
    val totalSpots: Long,
    val price: Long,
)
