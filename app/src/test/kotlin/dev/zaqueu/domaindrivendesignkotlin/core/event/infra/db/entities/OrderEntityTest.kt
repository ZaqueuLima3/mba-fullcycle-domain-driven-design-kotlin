package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.OrderEntity.Companion.toDomain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class OrderEntityTest {
    @Test
    fun `should convert an OrderEntity to an Order domain`() {
        val customer = CustomerEntity(
            id = UUID.randomUUID(),
            name = "name",
            cpf = "93928642057"
        )

        val partner = PartnerEntity(
            id = UUID.randomUUID(),
            name = "partner",
        )

        val event = EventEntity(
            id = UUID.randomUUID(),
            name = "name",
            description = "description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 1,
            totalSpotsReserved = 0,
            partner = partner
        )

        val eventSection = EventSectionEntity(
            id = UUID.randomUUID(),
            name = "name",
            description = "description",
            isPublished = false,
            totalSpots = 1,
            totalSpotsReserved = 0,
            price = 1000L,
            event = event
        )

        val eventSpot = EventSpotEntity(
            id = UUID.randomUUID(),
            isPublished = false,
            location = "",
            isReserved = false,
            eventSection = eventSection
        )

        val orderEntity = OrderEntity(
            id = UUID.randomUUID(),
            amount = 1000L,
            status = Order.Status.PENDING,
            customer = customer,
            eventSpot = eventSpot,
        )

        val order = orderEntity.toDomain()

        Assertions.assertInstanceOf(Order::class.java, order)
        Assertions.assertEquals(orderEntity.id.toString(), order.id.value)
        Assertions.assertEquals(orderEntity.status, order.status)
        Assertions.assertEquals(orderEntity.amount, order.amount)
        Assertions.assertEquals(orderEntity.customer.id.toString(), order.customerId.value)
        Assertions.assertEquals(orderEntity.eventSpot.id.toString(), order.eventSpotId.value)
    }

    @Test
    fun `should convert an Order domain to OrderEntity`() {
        val customer = CustomerEntity(
            id = UUID.randomUUID(),
            name = "name",
            cpf = "93928642057"
        )

        val partner = PartnerEntity(
            id = UUID.randomUUID(),
            name = "partner",
        )

        val event = EventEntity(
            id = UUID.randomUUID(),
            name = "name",
            description = "description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 1,
            totalSpotsReserved = 0,
            partner = partner
        )

        val eventSection = EventSectionEntity(
            id = UUID.randomUUID(),
            name = "name",
            description = "description",
            isPublished = false,
            totalSpots = 1,
            totalSpotsReserved = 0,
            price = 1000L,
            event = event
        )

        val eventSpot = EventSpotEntity(
            id = UUID.randomUUID(),
            isPublished = false,
            location = "",
            isReserved = false,
            eventSection = eventSection
        )

        val order = Order.create(
            amount = 1000L,
            status = Order.Status.PAID,
            customerId = customer.id.toString(),
            eventSpotId = eventSpot.id.toString()
        )

        val orderEntity = OrderEntity.fromDomain(order, customer, eventSpot)

        Assertions.assertInstanceOf(OrderEntity::class.java, orderEntity)
        Assertions.assertEquals(order.id.value, orderEntity.id.toString())
        Assertions.assertEquals(order.status, orderEntity.status)
        Assertions.assertEquals(order.amount, orderEntity.amount)
        Assertions.assertEquals(order.customerId.value, orderEntity.customer.id.toString())
        Assertions.assertEquals(order.eventSpotId.value, orderEntity.eventSpot.id.toString())
    }
}
