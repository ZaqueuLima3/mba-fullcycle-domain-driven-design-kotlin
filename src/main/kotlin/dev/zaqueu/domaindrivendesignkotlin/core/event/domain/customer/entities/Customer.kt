package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Cpf
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId

internal data class Customer(
    override val id: CustomerId,
    var name: String,
    val cpf: Cpf,
) : AggregateRoot() {

    constructor(
        id: String? = null,
        name: String,
        cpf: Cpf,
    ) : this(
        id = id.toDomainUuid(),
        name = name,
        cpf = cpf,
    )

    fun changeName(name: String) {
        this.name = name
    }

    companion object {
        fun create(name: String, cpf: String): Customer {
            return Customer(cpf = Cpf.create(cpf), name = name)
        }
    }
}
