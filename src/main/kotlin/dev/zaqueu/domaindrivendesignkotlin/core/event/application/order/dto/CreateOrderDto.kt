package dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.dto

internal data class CreateOrderDto(
    val customerId: String,
    val spotId: String,
    val sectionId: String,
    val eventId: String,
    val cardToken: String,
)
