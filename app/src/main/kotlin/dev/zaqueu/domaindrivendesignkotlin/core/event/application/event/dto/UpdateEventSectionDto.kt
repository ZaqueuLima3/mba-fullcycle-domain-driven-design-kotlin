package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto

internal data class UpdateEventSectionDto(
    val eventId: String,
    val sectionId: String,
    val name: String? = null,
    val description: String? = null,
    val price: Long? = null,
)
