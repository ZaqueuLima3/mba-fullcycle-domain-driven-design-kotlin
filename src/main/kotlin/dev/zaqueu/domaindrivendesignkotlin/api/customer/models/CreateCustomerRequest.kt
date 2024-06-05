package dev.zaqueu.domaindrivendesignkotlin.api.customer.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.CreateCustomerDto
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateCustomerRequest(
    val name: String,
    val cpf: String,
) {
    fun toInput() =  CreateCustomerDto(
        name = name,
        cpf = cpf,
    )
}
