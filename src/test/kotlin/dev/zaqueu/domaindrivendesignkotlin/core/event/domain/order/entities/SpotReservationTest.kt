package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class SpotReservationTest {
    @Test
    fun `should create an SpotReservation`() {
        val expectedSpotId = UUID.randomUUID().toString()
        val expectedReservationDate = Instant.now()
        val expectedCustomerId = UUID.randomUUID().toString()

        val spotReservation = SpotReservation.create(
            id = expectedSpotId,
            reservationDate = expectedReservationDate,
            customerId = expectedCustomerId,
        )

        Assertions.assertNotNull(spotReservation.id)
        Assertions.assertEquals(expectedReservationDate, spotReservation.reservationDate)
        Assertions.assertEquals(expectedCustomerId, spotReservation.customerId.value)
    }
}
