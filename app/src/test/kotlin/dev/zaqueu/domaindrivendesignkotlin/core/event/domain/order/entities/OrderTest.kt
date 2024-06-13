package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class OrderTest {
    @Test
    fun `should create an Order`() {
        val expectedAmount = 1000L
        val expectedStatus = Order.Status.PENDING
        val expectedCustomerId = UUID.randomUUID().toString()
        val expectedSpotId = UUID.randomUUID().toString()

        val order = Order.create(
            amount = expectedAmount,
            status = expectedStatus,
            customerId = expectedCustomerId,
            eventSpotId = expectedSpotId
        )

        Assertions.assertNotNull(order.id)
        Assertions.assertEquals(expectedAmount, order.amount)
        Assertions.assertEquals(expectedStatus, order.status)
        Assertions.assertEquals(expectedCustomerId, order.customerId.value)
        Assertions.assertEquals(expectedSpotId, order.eventSpotId.value)
    }

    @Test
    fun `should pay an order`() {
        val expectedAmount = 1000L
        val expectedStatus = Order.Status.PENDING
        val expectedCustomerId = UUID.randomUUID().toString()
        val expectedSpotId = UUID.randomUUID().toString()

        val order = Order.create(
            amount = expectedAmount,
            status = expectedStatus,
            customerId = expectedCustomerId,
            eventSpotId = expectedSpotId
        )

        Assertions.assertTrue(order.status == Order.Status.PENDING)

        order.pay()
        Assertions.assertTrue(order.status == Order.Status.PAID)
    }

    @Test
    fun `should cancel an order`() {
        val expectedAmount = 1000L
        val expectedStatus = Order.Status.PENDING
        val expectedCustomerId = UUID.randomUUID().toString()
        val expectedSpotId = UUID.randomUUID().toString()

        val order = Order.create(
            amount = expectedAmount,
            status = expectedStatus,
            customerId = expectedCustomerId,
            eventSpotId = expectedSpotId
        )

        Assertions.assertTrue(order.status == Order.Status.PENDING)

        order.cancel()
        Assertions.assertTrue(order.status == Order.Status.CANCELLED)
    }
}
