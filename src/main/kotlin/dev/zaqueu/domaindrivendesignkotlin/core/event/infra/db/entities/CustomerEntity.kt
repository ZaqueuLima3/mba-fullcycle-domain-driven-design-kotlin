package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Cpf
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "customers")
internal class CustomerEntity(
    @Id
    @Column(name = "id")
    var id: UUID,
    @Column(name = "name")
    var name: String,
    @Column(name = "cpf", unique = true)
    var cpf: String,
) {
    companion object {
        fun fromDomain(customer: Customer): CustomerEntity {
            return CustomerEntity(
                id = customer.id.toUUID(),
                name = customer.name,
                cpf = customer.cpf.value,
            )
        }

        fun CustomerEntity.toDomain(): Customer {
            return Customer(
                id = id.toString(),
                name = name,
                cpf = Cpf.create(cpf),
            )
        }
    }
}
