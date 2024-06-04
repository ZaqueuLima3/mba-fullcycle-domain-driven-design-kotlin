package dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.services

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.dto.CreateOrderDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.payment.gateway.PaymentGateway
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.SpotReservation
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.OrderRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.SpotReservationRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class OrderServiceTest {
    @MockK
    internal lateinit var eventRepository: EventRepository

    @MockK
    internal lateinit var customerRepository: CustomerRepository

    @MockK
    internal lateinit var spotReservationRepository: SpotReservationRepository

    @MockK
    internal lateinit var orderRepository: OrderRepository

    @MockK
    internal lateinit var paymentGateway: PaymentGateway

    private lateinit var orderService: OrderService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        orderService = OrderService(
            orderRepository,
            eventRepository,
            customerRepository,
            spotReservationRepository,
            paymentGateway,
        )

        every {
            orderRepository.add(any())
        } just Runs

        every {
            spotReservationRepository.add(any())
        } just Runs

        every {
            eventRepository.add(any())
        } just Runs
    }

    @Test
    fun `should list all orders`() {
        val expectedOrders = listOf(
            Order.create(
                amount = 1000,
                status = Order.Status.PENDING,
                customerId = UUID.randomUUID().toString(),
                eventSpotId = UUID.randomUUID().toString()
            )
        )

        every {
            orderRepository.findAll()
        } returns expectedOrders

        val orders = orderService.list()

        Assertions.assertEquals(expectedOrders, orders)

        verify {
            orderRepository.findAll()
        }
        confirmVerified(orderRepository)
    }

    @Test
    fun `should create a new order when the event is published`() {
        val customer = Customer.create(
            name = "name",
            cpf = "93928642057"
        )

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        val spot = section.spots.first()

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        event.addSections(setOf(section))
        event.publishAll()

        val input = CreateOrderDto(
            customerId = customer.id.value,
            spotId = spot.id.value,
            sectionId = section.id.value,
            eventId = event.id.value,
            cardToken = UUID.randomUUID().toString()
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns customer

        every {
            eventRepository.findById(any())
        } returns event

        every {
            paymentGateway.payment(any(), any())
        } just Runs

        val order = orderService.create(input)

        Assertions.assertNotNull(order.id)
        Assertions.assertEquals(input.customerId, order.customerId.value)
        Assertions.assertEquals(input.spotId, order.eventSpotId.value)
        Assertions.assertEquals(Order.Status.PAID, order.status)
        Assertions.assertEquals(section.price, order.amount)

        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
            eventRepository.findById(any())

            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())

        }
        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should not create a new order when the event is not published`() {
        val expectedErrorMessage = "Spot not available"

        val customer = Customer.create(
            name = "name",
            cpf = "93928642057"
        )

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        val spot = section.spots.first()

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        event.addSections(setOf(section))

        val input = CreateOrderDto(
            customerId = customer.id.value,
            spotId = spot.id.value,
            sectionId = section.id.value,
            eventId = event.id.value,
            cardToken = UUID.randomUUID().toString()
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns customer

        every {
            eventRepository.findById(any())
        } returns event

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
            eventRepository.findById(any())
        }

        verify(exactly = 0) {
            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())
        }

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should not create a new order with a non existent spot`() {
        val expectedSpotId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Spot not found with ID: $expectedSpotId"

        val customer = Customer.create(
            name = "name",
            cpf = "93928642057"
        )

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        event.addSections(setOf(section))

        val input = CreateOrderDto(
            customerId = customer.id.value,
            spotId = expectedSpotId,
            sectionId = section.id.value,
            eventId = event.id.value,
            cardToken = UUID.randomUUID().toString()
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns customer

        every {
            eventRepository.findById(any())
        } returns event

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
            eventRepository.findById(any())
        }

        verify(exactly = 0) {
            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())
        }

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should not create a new order with a non existent section`() {
        val expectedSectionId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Section not found with ID: $expectedSectionId"

        val customer = Customer.create(
            name = "name",
            cpf = "93928642057"
        )

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        val input = CreateOrderDto(
            customerId = customer.id.value,
            spotId = UUID.randomUUID().toString(),
            sectionId = expectedSectionId,
            eventId = event.id.value,
            cardToken = UUID.randomUUID().toString()
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns customer

        every {
            eventRepository.findById(any())
        } returns event

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
            eventRepository.findById(any())
        }

        verify(exactly = 0) {
            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())
        }

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should not create a new order with a non existent event`() {
        val expectedEventId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Event not found with ID: $expectedEventId"

        val customer = Customer.create(
            name = "name",
            cpf = "93928642057"
        )

        val input = CreateOrderDto(
            customerId = customer.id.value,
            spotId = UUID.randomUUID().toString(),
            sectionId = UUID.randomUUID().toString(),
            eventId = expectedEventId,
            cardToken = UUID.randomUUID().toString(),
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns customer

        every {
            eventRepository.findById(any())
        } returns null

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
            eventRepository.findById(any())
        }

        verify(exactly = 0) {
            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())
        }

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should not create a new order with a non existent customer`() {
        val expectedCustomerId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Customer not found with ID: $expectedCustomerId"

        val input = CreateOrderDto(
            customerId = expectedCustomerId,
            spotId = UUID.randomUUID().toString(),
            sectionId = UUID.randomUUID().toString(),
            eventId = UUID.randomUUID().toString(),
            cardToken = UUID.randomUUID().toString(),
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns null

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
        }

        verify(exactly = 0) {
            eventRepository.findById(any())
            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())
        }

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should not create a duplicated order`() {
        val expectedErrorMessage = "Spot already reserved"

        val spotReservation = SpotReservation.create(
            customerId = UUID.randomUUID().toString(),
            id = UUID.randomUUID().toString(),
            reservationDate = Instant.now()
        )

        val input = CreateOrderDto(
            customerId = UUID.randomUUID().toString(),
            spotId = UUID.randomUUID().toString(),
            sectionId = UUID.randomUUID().toString(),
            eventId = UUID.randomUUID().toString(),
            cardToken = UUID.randomUUID().toString(),
        )

        every {
            spotReservationRepository.findById(any())
        } returns spotReservation

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)

        verifySequence {
            spotReservationRepository.findById(any())
        }

        verify(exactly = 0) {
            customerRepository.findById(any())
            eventRepository.findById(any())
            spotReservationRepository.add(any())
            eventRepository.add(any())
            orderRepository.add(any())
        }

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }

    @Test
    fun `should throws an exception and mark order as cancelled if anything goes wrong`() {
        val expectedErrorMessage = "Payment failed"

        val customer = Customer.create(
            name = "name",
            cpf = "93928642057"
        )

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        val spot = section.spots.first()

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        event.addSections(setOf(section))
        event.publishAll()

        val input = CreateOrderDto(
            customerId = customer.id.value,
            spotId = spot.id.value,
            sectionId = section.id.value,
            eventId = event.id.value,
            cardToken = UUID.randomUUID().toString()
        )

        every {
            spotReservationRepository.findById(any())
        } returns null

        every {
            customerRepository.findById(any())
        } returns customer

        every {
            eventRepository.findById(any())
        } returns event

        every {
            paymentGateway.payment(any(), any())
        } throws Exception(expectedErrorMessage)

        val actualException = Assertions.assertThrows(Exception::class.java) {
            orderService.create(input)
        }

        Assertions.assertTrue(actualException.message?.contains(expectedErrorMessage) == true)

        val orderSlot = slot<Order>()
        verifySequence {
            spotReservationRepository.findById(any())
            customerRepository.findById(any())
            eventRepository.findById(any())

            spotReservationRepository.add(any())
            orderRepository.add(capture(orderSlot))
        }
        Assertions.assertEquals(Order.Status.CANCELLED, orderSlot.captured.status)

        confirmVerified(
            spotReservationRepository,
            customerRepository,
            eventRepository,
            orderRepository,
        )
    }
}
