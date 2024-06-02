package dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Cpf
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.CreateCustomerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.dto.UpdateCustomerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.services.CustomerService
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class CustomerServiceTest {
    @MockK
    internal lateinit var customerRepository: CustomerRepository

    @MockK
    internal lateinit var unitOfWork: UnitOfWork

    private lateinit var customerService: CustomerService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        customerService = CustomerService(customerRepository, unitOfWork)

        every {
            customerRepository.add(any())
        } just Runs

        every {
            unitOfWork.commit()
        } just Runs
    }

    @Test
    fun `should list all customers`() {
        val expectedCustomers = listOf(
            Customer.create(
                name = "John Doe",
                cpf = "93928642057",
            )
        )

        every {
            customerRepository.findAll()
        } returns expectedCustomers

        val customers = customerService.list()

        Assertions.assertEquals(expectedCustomers, customers)

        verify {
            customerRepository.findAll()
        }
        confirmVerified(customerRepository)
    }

    @Test
    fun `should register a new customer`() {
        val input = CreateCustomerDto(
            name = "John Doe",
            cpf = "93928642057",
        )

        val customer = customerService.register(input)

        Assertions.assertNotNull(customer.id)
        Assertions.assertEquals(input.name, customer.name)
        Assertions.assertEquals(input.cpf, customer.cpf.value)

        verify {
            customerRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(customerRepository, unitOfWork)
    }

    @Test
    fun `should update the customer name`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateCustomerDto(
            id = expectedId,
            name = "John Doe",
        )

        val customer = Customer(
            id = expectedId,
            name = "John",
            cpf = Cpf.create("93928642057")
        )

        every {
            customerRepository.findById(customer.id)
        } returns customer

        val updatedCustomer = customerService.update(input)

        Assertions.assertEquals(input.name, updatedCustomer.name)

        verify {
            customerRepository.findById(customer.id)
            customerRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(customerRepository, unitOfWork)
    }

    @Test
    fun `should not update the customer name whe it is empty or null`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateCustomerDto(
            id = expectedId,
            name = null,
        )

        val customer = Customer(
            id = expectedId,
            name = "John",
            cpf = Cpf.create("93928642057")
        )

        every {
            customerRepository.findById(customer.id)
        } returns customer

        val updatedCustomer = customerService.update(input)

        Assertions.assertEquals(customer.name, updatedCustomer.name)

        verify {
            customerRepository.findById(customer.id)
            customerRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(customerRepository, unitOfWork)
    }
}
