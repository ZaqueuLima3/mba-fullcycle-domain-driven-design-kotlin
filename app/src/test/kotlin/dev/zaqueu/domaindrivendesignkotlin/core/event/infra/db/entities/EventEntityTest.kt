package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventEntity.Companion.toDomain
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
class EventEntityTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    @Transactional
    fun `should persist and retrieve EventEntity`() {
        val partner = PartnerEntity.fromDomain(
            Partner.create(
                "Test name"
            )
        )
        val eventEntity = EventEntity(
            id = UUID.randomUUID(),
            name = "Test Event",
            description = "some description",
            date = Instant.now(),
            isPublished = false,
            totalSpots = 0,
            totalSpotsReserved = 0,
            partner = partner,
        )

        testEntityManager.persist(partner)
        testEntityManager.persist(eventEntity)
        entityManager.flush()
        entityManager.clear()

        val foundEventEntity = entityManager.find(EventEntity::class.java, eventEntity.id)
        Assertions.assertNotNull(foundEventEntity)
        Assertions.assertEquals(eventEntity.name, foundEventEntity.name)
        Assertions.assertEquals(partner.id.toString(), foundEventEntity.partner.id.toString())
    }

    @Test
    fun `should convert a EventEntity to Event domain`() {
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

        val event = eventEntity.toDomain()

        Assertions.assertInstanceOf(Event::class.java, event)
        Assertions.assertEquals(eventEntity.id.toString(), event.id.value)
        Assertions.assertEquals(eventEntity.name, event.name)
        Assertions.assertEquals(partner.id.value, event.partnerId.value)
    }

    @Test
    fun `should convert a Event domain to EventEntity`() {
        val partner = Partner.create(
            "Test name"
        )

        val event = Event.create(
            name = "Test Event",
            description = "some description",
            date = Instant.now(),
            partnerId = partner.id.value,
        )

        val eventEntity = EventEntity.fromDomain(event, PartnerEntity.fromDomain(partner))

        Assertions.assertInstanceOf(EventEntity::class.java, eventEntity)
        Assertions.assertEquals(event.id.value, eventEntity.id.toString())
        Assertions.assertEquals(event.name, eventEntity.name)
        Assertions.assertEquals(partner.id.value, eventEntity.partner.id.toString())
    }
}
