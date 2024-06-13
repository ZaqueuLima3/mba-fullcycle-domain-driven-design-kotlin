package dev.zaqueu.domaindrivendesignkotlin.api.customer.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import kotlinx.serialization.Serializable

@Serializable
internal data class CustomerListResponse(
    val id: String,
    val name: String,
    val cpf: String,
) {
    companion object {
        fun fromDomain(customer: Customer) = CustomerListResponse(
            id = customer.id.value,
            name = customer.name,
            cpf = customer.cpf.value,
        )
    }
}
