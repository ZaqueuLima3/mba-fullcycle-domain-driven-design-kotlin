package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.CustomerEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class CustomerMysqlRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Test
    @Transactional
    fun `should add a new customer`() {
        val customer = Customer.create(
            name = "Test Customer",
            cpf = "93928642057",
        )

        customerRepository.add(customer)
        entityManager.flush()
        entityManager.clear()

        val customerEntity = entityManager.find(CustomerEntity::class.java, UUID.fromString(customer.id.value))
        Assertions.assertNotNull(customerEntity)
        Assertions.assertEquals(customer.name, customerEntity.name)
    }

    @Test
    @Transactional
    fun `should throws an Exception when try to add customer with the same Cpf`() {
        val customer1 = Customer.create(
            name = "Test Customer one",
            cpf = "93928642057",
        )
        val customer2 = Customer.create(
            name = "Test Customer two",
            cpf = "93928642057",
        )

        customerRepository.add(customer1)
        customerRepository.add(customer2)

        val actualException = Assertions.assertThrows(RuntimeException::class.java) {
            entityManager.flush()
        }

        Assertions.assertTrue(actualException.message?.contains("Unique index") == true)
    }

    @Test
    @Transactional
    fun `should return a customer when it is found`() {
        val customer = Customer.create(
            name = "Test Customer",
            cpf = "93928642057",
        )

        customerRepository.add(customer)
        entityManager.flush()
        entityManager.clear()

        val savedCustomer = customerRepository.findById(customer.id)
        Assertions.assertNotNull(savedCustomer)
        Assertions.assertEquals(customer.name, savedCustomer?.name)
    }

    @Test
    fun `should return null when customer is no found`() {
        val savedCustomer = customerRepository.findById(CustomerId(UUID.randomUUID().toString()))
        Assertions.assertNull(savedCustomer)
    }

    @Test
    @Transactional
    fun `should return a list of customers`() {
        val customer1 = Customer.create(
            name = "Test Customer one",
            cpf = "93928642057",
        )
        val customer2 = Customer.create(
            name = "Test Customer two",
            cpf = "77519720098",
        )

        customerRepository.add(customer1)
        customerRepository.add(customer2)
        entityManager.flush()
        entityManager.clear()

        val customers = customerRepository.findAll()

        Assertions.assertTrue(customers.size == 2)
        Assertions.assertTrue(customers.contains(customer1))
        Assertions.assertTrue(customers.contains(customer2))
    }

    @Test
    @Transactional
    fun `should return an empty list of customers`() {
        val customers = customerRepository.findAll()
        Assertions.assertTrue(customers.isEmpty())
    }

    @Test
    @Transactional
    fun `should delete a customer when it is found`() {
        val customer = Customer.create(
            name = "Test Customer",
            cpf = "93928642057",
        )
        customerRepository.add(customer)
        entityManager.flush()
        entityManager.clear()

        val savedCustomer = entityManager.find(CustomerEntity::class.java, UUID.fromString(customer.id.value))
        Assertions.assertNotNull(savedCustomer)
        Assertions.assertEquals(customer.name, savedCustomer?.name)

        customerRepository.delete(customer.id)

        val deletedCustomer = entityManager.find(CustomerEntity::class.java, UUID.fromString(customer.id.value))

        Assertions.assertNull(deletedCustomer)
    }

    @Test
    fun `should do nothing when doesn't find a customer to delete`() {
        Assertions.assertDoesNotThrow {
            customerRepository.delete(CustomerId(UUID.randomUUID().toString()))
        }
    }
}
