package dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.CreateCustomerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.CreateCustomerDto.Companion.toDomain
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.UpdateCustomerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.toCustomerId
import org.springframework.stereotype.Service

@Service
internal class CustomerService(
    private val customerRepository: CustomerRepository,
    private val unitOfWork: UnitOfWork
) {
    fun list(): List<Customer> {
        return customerRepository.findAll()
    }

    fun register(input: CreateCustomerDto): Customer {
        val customer = input.toDomain()
        customerRepository.add(customer)
        unitOfWork.commit()
        return customer
    }

    fun update(input: UpdateCustomerDto): Customer {
        val customer = customerRepository.findById(input.id.toCustomerId()) ?: throw Exception("Customer not found")

        if (!input.name.isNullOrBlank()) customer.changeName(input.name)

        customerRepository.add(customer)
        unitOfWork.commit()

        return customer
    }
}
