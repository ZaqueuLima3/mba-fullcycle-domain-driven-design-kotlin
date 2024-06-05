package dev.zaqueu.domaindrivendesignkotlin.api.customer.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.UpdateCustomerDto
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateCustomerRequest(
    val name: String,
) {
    fun toInput(id: String) = UpdateCustomerDto(
        id = id,
        name = name,
    )
}
