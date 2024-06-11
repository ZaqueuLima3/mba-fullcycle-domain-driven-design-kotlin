package dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.ResourceNotFoundException
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.CreateCustomerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.CreateCustomerDto.Companion.toDomain
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.UpdateCustomerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class CustomerService(
    private val customerRepository: CustomerRepository,
) {
    fun list(): List<Customer> {
        return customerRepository.findAll()
    }

    @Transactional
    fun register(input: CreateCustomerDto): Customer {
        val customer = input.toDomain()
        customerRepository.add(customer)
        return customer
    }

    @Transactional
    fun findById(id: String): Customer? {
        return customerRepository.findById(id.toDomainUuid<CustomerId>())
    }

    @Transactional
    fun update(input: UpdateCustomerDto): Customer {
        val customer = customerRepository.findById(input.id.toDomainUuid<CustomerId>())
            ?: throw ResourceNotFoundException("Customer with id ${input.id} not found")

        if (!input.name.isNullOrBlank()) customer.changeName(input.name)

        customerRepository.update(customer)

        return customer
    }

    @Transactional
    fun delete(id: String) {
        customerRepository.delete(id.toDomainUuid<CustomerId>())
    }
}
