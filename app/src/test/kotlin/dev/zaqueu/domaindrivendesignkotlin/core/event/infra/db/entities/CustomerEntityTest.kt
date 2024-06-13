package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.CustomerEntity.Companion.toDomain
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class CustomerEntityTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    @Transactional
    fun `should persist and retrieve CustomerEntity`() {
        val customerEntity = CustomerEntity(
            id = UUID.randomUUID(),
            name = "Test Customer",
            cpf = "93928642057"
        )

        testEntityManager.persist(customerEntity)
        entityManager.flush()
        entityManager.clear()

        val foundCustomerEntity = entityManager.find(CustomerEntity::class.java, customerEntity.id)
        Assertions.assertNotNull(foundCustomerEntity)
        Assertions.assertEquals(customerEntity.name, foundCustomerEntity.name)
    }

    @Test
    fun `should convert a CustomerEntity to Customer domain`() {
        val customerEntity = CustomerEntity(
            id = UUID.randomUUID(),
            name = "Test Customer",
            cpf = "93928642057",
        )

        val customer = customerEntity.toDomain()

        Assertions.assertInstanceOf(Customer::class.java, customer)
        Assertions.assertEquals(customerEntity.name, customer.name)
        Assertions.assertEquals(customerEntity.cpf, customer.cpf.value)
    }

    @Test
    fun `should convert a Customer domain to CustomerEntity`() {
        val customer = Customer.create(
            name = "Test Customer",
            cpf = "93928642057"
        )

        val customerEntity = CustomerEntity.fromDomain(customer)

        Assertions.assertInstanceOf(CustomerEntity::class.java, customerEntity)
        Assertions.assertEquals(customer.cpf.value, customerEntity.cpf)
    }
}
