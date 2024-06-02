package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto

internal data class UpdateEventSpotLocationDto(
    val eventId: String,
    val sectionId: String,
    val spotId: String,
    val location: String,
)
