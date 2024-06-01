package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSectionEntity.Companion.toDomain
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
import java.time.Instant
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class EventSectionSectionEntityTest {
    @Test
    fun `should convert a EventSectionEntity to EventSection domain`() {
        val eventEntity = EventEntity(
            id = UUID.randomUUID(),
            name = "Test Event",
            description = "some description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID(),
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

        val event = eventSectionEntity.toDomain()

        Assertions.assertInstanceOf(EventSection::class.java, event)
        Assertions.assertEquals(eventSectionEntity.id.toString(), event.id.value)
        Assertions.assertEquals(eventSectionEntity.name, event.name)
    }

    @Test
    fun `should convert a EventSection domain to EventSectionEntity`() {
        val eventEntity = EventEntity(
            id = UUID.randomUUID(),
            name = "Test Event",
            description = "some description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID(),
        )

        val eventSection = EventSection.create(
            name = "Test EventSection",
            description = "some description",
            totalSpots = 0,
            price = 100,
        )

        val eventSectionEntity = EventSectionEntity.fromDomain(eventSection, eventEntity)

        Assertions.assertInstanceOf(EventSectionEntity::class.java, eventSectionEntity)
        Assertions.assertEquals(eventSection.id.value, eventSectionEntity.id.toString())
        Assertions.assertEquals(eventSection.name, eventSectionEntity.name)
    }
}
