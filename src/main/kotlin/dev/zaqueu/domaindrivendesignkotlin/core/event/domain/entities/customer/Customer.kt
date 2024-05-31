package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.customer

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Cpf

internal data class Customer(
    override val id: CustomerId,
    val name: String,
    val cpf: Cpf,
) : AggregateRoot() {

    constructor(
        id: String? = null,
        name: String,
        cpf: Cpf,
    ) : this(
        id = if (id != null) CustomerId(id) else CustomerId(),
        name = name,
        cpf = cpf,
    )

    companion object {
        fun create(name: String, cpf: String): Customer {
            return Customer(cpf = Cpf.create(cpf), name = name)
        }
    }
}
