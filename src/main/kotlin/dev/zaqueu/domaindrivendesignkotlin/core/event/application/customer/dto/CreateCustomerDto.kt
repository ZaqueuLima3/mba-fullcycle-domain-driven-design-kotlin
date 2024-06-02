package dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer

internal data class CreateCustomerDto(
    val name: String,
    val cpf: String,
) {
    companion object {
        fun CreateCustomerDto.toDomain(): Customer {
            return Customer.create(
                name = name,
                cpf = cpf,
            )
        }
    }
}
