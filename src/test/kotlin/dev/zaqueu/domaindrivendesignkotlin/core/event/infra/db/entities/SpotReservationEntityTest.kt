package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.SpotReservation
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.SpotReservationEntity.Companion.toDomain
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
class SpotReservationEntityTest {
    @Test
    fun `should convert an SpotReservationEntity to an SpotReservation domain`() {
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

        val spotReservationEntity = SpotReservationEntity(
            id = eventSpot.id,
            reservationDate = Instant.now(),
            eventSpot = eventSpot,
            customer = customer,
        )

        val spotReservation = spotReservationEntity.toDomain()

        Assertions.assertInstanceOf(SpotReservation::class.java, spotReservation)
        Assertions.assertEquals(spotReservationEntity.id.toString(), spotReservation.id.value)
        Assertions.assertEquals(spotReservationEntity.eventSpot.id.toString(), spotReservation.id.value)
        Assertions.assertEquals(spotReservationEntity.reservationDate, spotReservation.reservationDate)
        Assertions.assertEquals(spotReservationEntity.customer.id.toString(), spotReservation.customerId.value)
    }

    @Test
    fun `should convert an SpotReservation domain to SpotReservationEntity`() {
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

        val spotReservation = SpotReservation.create(
            id = eventSpot.id.toString(),
            reservationDate = Instant.now(),
            customerId = customer.id.toString(),
        )

        val spotReservationEntity = SpotReservationEntity.fromDomain(spotReservation, customer, eventSpot)

        Assertions.assertInstanceOf(SpotReservationEntity::class.java, spotReservationEntity)
        Assertions.assertEquals(spotReservation.id.value, spotReservationEntity.id.toString())
        Assertions.assertEquals(eventSpot.id.toString(), spotReservationEntity.id.toString())
        Assertions.assertEquals(spotReservation.reservationDate, spotReservationEntity.reservationDate)
        Assertions.assertEquals(spotReservation.customerId.value, spotReservationEntity.customer.id.toString())
    }
}
