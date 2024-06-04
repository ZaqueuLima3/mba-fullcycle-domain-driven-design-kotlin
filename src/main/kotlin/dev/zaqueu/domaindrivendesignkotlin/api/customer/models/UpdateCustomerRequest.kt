package dev.zaqueu.domaindrivendesignkotlin.api.customer.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.UpdateCustomerDto

internal data class UpdateCustomerRequest(
    val name: String,
) {
    fun toInput(id: String) = UpdateCustomerDto(
        id = id,
        name = name,
    )
}
