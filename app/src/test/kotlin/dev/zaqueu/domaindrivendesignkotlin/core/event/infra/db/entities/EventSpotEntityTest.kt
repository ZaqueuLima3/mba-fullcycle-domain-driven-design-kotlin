package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSpotEntity.Companion.toDomain
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
class EventSpotEntityTest {
    @Test
    fun `should convert a EventSpotEntity to EventSpot domain`() {
        val partner = Partner.create(
            "Test name"
        )
        val eventEntity = EventEntity(
            id = UUID.randomUUID(),
            name = "Test Event",
            description = "some description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            partner = PartnerEntity.fromDomain(partner),
        )

        val eventSectionEntity = EventSectionEntity(
            id = UUID.randomUUID(),
            name = "Test EventSection",
            description = "some description",
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            price = 1000,
            event = eventEntity,
        )

        val eventSpotEntity = EventSpotEntity(
            id = UUID.randomUUID(),
            location = "Test Location",
            isPublished = false,
            isReserved = false,
            eventSection = eventSectionEntity,
        )

        val event = eventSpotEntity.toDomain()

        Assertions.assertInstanceOf(EventSpot::class.java, event)
        Assertions.assertEquals(eventSpotEntity.id.toString(), event.id.value)
        Assertions.assertEquals(eventSpotEntity.location, event.location)
    }

    @Test
    fun `should convert a EventSpot domain to EventSpotEntity`() {
        val partner = Partner.create(
            "Test name"
        )
        val eventEntity = EventEntity(
            id = UUID.randomUUID(),
            name = "Test Event",
            description = "some description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            partner = PartnerEntity.fromDomain(partner),
        )

        val eventSectionEntity = EventSectionEntity(
            id = UUID.randomUUID(),
            name = "Test EventSection",
            description = "some description",
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            price = 1000,
            event = eventEntity,
        )

        val eventSpot = EventSpot.create()

        val eventSpotEntity = EventSpotEntity.fromDomain(eventSpot, eventSectionEntity)

        Assertions.assertInstanceOf(EventSpotEntity::class.java, eventSpotEntity)
        Assertions.assertEquals(eventSpot.id.value, eventSpotEntity.id.toString())
        Assertions.assertEquals(eventSpot.location, eventSpotEntity.location)
    }
}
