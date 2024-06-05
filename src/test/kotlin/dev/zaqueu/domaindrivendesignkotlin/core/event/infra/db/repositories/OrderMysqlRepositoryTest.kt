package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.OrderRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.valueobject.OrderId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.OrderEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@IntegrationTest
class OrderMysqlRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    private lateinit var customer: Customer

    private lateinit var event: Event

    private lateinit var partner: Partner

    private lateinit var spotId: EventSpotId

    @BeforeEach
    fun setup() {
        customer = Customer.create(
            name = "Test Order",
            cpf = "93928642057",
        )

        partner = Partner.create(
            name = "Test Name"
        )

        event = Event.create(
            name = "event",
            description = "description",
            date = Instant.now(),
            partnerId = partner.id.value
        )

        event.addSection(
            name = "Section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        spotId = event.sections.first().spots.first().id

        customerRepository.add(customer)
        partnerRepository.add(partner)
        eventRepository.add(event)

        entityManager.flush()
        entityManager.clear()
    }

    @Test
    @Transactional
    fun `should add a new order`() {
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = customer.id.value,
            eventSpotId = spotId.value,
        )

        orderRepository.add(order)
        entityManager.flush()
        entityManager.clear()

        val orderEntity = entityManager.find(OrderEntity::class.java, order.id.toUUID())
        Assertions.assertNotNull(orderEntity)
        Assertions.assertEquals(order.id.value, orderEntity.id.toString())
    }

    @Test
    @Transactional
    fun `should update a order`() {
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = customer.id.value,
            eventSpotId = spotId.value,
        )

        orderRepository.add(order)
        entityManager.flush()
        entityManager.clear()

        var orderEntity = entityManager.find(OrderEntity::class.java, order.id.toUUID())
        Assertions.assertNotNull(orderEntity)
        Assertions.assertEquals(order.id.value, orderEntity.id.toString())

        order.pay()
        orderRepository.update(order)
        entityManager.flush()
        entityManager.clear()

        orderEntity = entityManager.find(OrderEntity::class.java, order.id.toUUID())
        Assertions.assertNotNull(orderEntity)
        Assertions.assertEquals(Order.Status.PAID, orderEntity.status)
    }

    @Test
    @Transactional
    fun `should throws an Exception when try to add order with a non existent customer`() {
        val expectedErrorMessage = "Customer not found"
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = UUID.randomUUID().toString(),
            eventSpotId = spotId.value,
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderRepository.add(order)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    @Transactional
    fun `should throws an Exception when try to add order with a non existent spot`() {
        val expectedErrorMessage = "Spot not found"
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = customer.id.value,
            eventSpotId = UUID.randomUUID().toString(),
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderRepository.add(order)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }


    @Test
    @Transactional
    fun `should return an order when it is found`() {
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = customer.id.value,
            eventSpotId = spotId.value,
        )

        orderRepository.add(order)
        entityManager.flush()
        entityManager.clear()

        val savedOrder = orderRepository.findById(order.id)
        Assertions.assertNotNull(savedOrder)
        Assertions.assertEquals(order.id, savedOrder?.id)
    }

    @Test
    @Transactional
    fun `should return null when order is no found`() {
        val savedOrder = orderRepository.findById(UUID.randomUUID().toDomainUuid<OrderId>())
        Assertions.assertNull(savedOrder)
    }

    @Test
    @Transactional
    fun `should return a list of orders`() {
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = customer.id.value,
            eventSpotId = spotId.value,
        )

        orderRepository.add(order)
        entityManager.flush()
        entityManager.clear()

        val orders = orderRepository.findAll()

        Assertions.assertTrue(orders.size == 1)
        Assertions.assertTrue(orders.contains(order))
    }

    @Test
    @Transactional
    fun `should return an empty list of orders`() {
        val orders = orderRepository.findAll()
        Assertions.assertTrue(orders.isEmpty())
    }

    @Test
    @Transactional
    fun `should delete an order when it is found`() {
        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PENDING,
            customerId = customer.id.value,
            eventSpotId = spotId.value,
        )

        orderRepository.add(order)
        entityManager.flush()
        entityManager.clear()

        val savedOrder = entityManager.find(OrderEntity::class.java, order.id.toUUID())
        Assertions.assertNotNull(savedOrder)
        Assertions.assertEquals(order.id.value, savedOrder?.id.toString())

        orderRepository.delete(order.id)

        val deletedOrder = entityManager.find(OrderEntity::class.java, order.id.toUUID())

        Assertions.assertNull(deletedOrder)
    }

    @Test
    @Transactional
    fun `should do nothing when doesn't find an order to delete`() {
        Assertions.assertDoesNotThrow {
            orderRepository.delete(UUID.randomUUID().toDomainUuid<OrderId>())
        }
    }
}
