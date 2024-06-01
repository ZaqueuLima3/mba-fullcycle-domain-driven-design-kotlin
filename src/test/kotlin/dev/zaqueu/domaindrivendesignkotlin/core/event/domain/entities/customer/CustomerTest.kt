package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.customer

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Cpf
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.customer.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.customer.CustomerId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class CustomerTest {
    @Test
    fun `should create a Customer`() {
        val expectedName = "Jhon Doe"
        val expectedCpf = "93928642057"

        val customer = Customer.create(
            name = expectedName,
            cpf = "939.286.420-57",
        )

        Assertions.assertNotNull(customer.id)
        Assertions.assertDoesNotThrow { UUID.fromString(customer.id.value) }
        Assertions.assertInstanceOf(CustomerId::class.java, customer.id)
        Assertions.assertEquals(expectedName, customer.name)
        Assertions.assertEquals(expectedCpf, customer.cpf.value)
    }

    @Test
    fun `should create a Customer with an id`() {
        val expectedId = UUID.randomUUID().toString()
        val expectedName = "Jhon Doe"
        val expectedCpf = Cpf.create("93928642057")

        val customer = Customer(
            id = expectedId,
            name =  expectedName,
            cpf = expectedCpf,
        )

        Assertions.assertEquals(expectedId, customer.id.value)
        Assertions.assertEquals(expectedName, customer.name)
        Assertions.assertEquals(expectedCpf.value, customer.cpf.value)
    }

    @Test
    fun `should create a Customer with a CustomerId`() {
        val expectedId = CustomerId(UUID.randomUUID().toString())
        val expectedName = "Jhon Doe"
        val expectedCpf = Cpf.create("93928642057")

        val customer = Customer(
            id = expectedId,
            name =  expectedName,
            cpf = expectedCpf,
        )

        Assertions.assertEquals(expectedId.value, customer.id.value)
        Assertions.assertEquals(expectedName, customer.name)
        Assertions.assertEquals(expectedCpf.value, customer.cpf.value)
    }
}
