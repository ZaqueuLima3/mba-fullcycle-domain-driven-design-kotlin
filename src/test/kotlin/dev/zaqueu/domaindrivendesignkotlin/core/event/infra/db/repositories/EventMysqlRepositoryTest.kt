package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class EventMysqlRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Test
    @Transactional
    fun `should add a new event`() {
        val partner = Partner.create(
            name = "Test Name"
        )

        val section = EventSection.create(
            name = "Section",
            description = "Some section description",
            totalSpots = 5L,
            price = 1000,
        )

        val event = partner.initializeEvent(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
            sections = mutableSetOf(section)
        )

        partnerRepository.add(partner)
        eventRepository.add(event)
        entityManager.flush()
        entityManager.clear()

        val eventEntity = entityManager.find(EventEntity::class.java, event.id.toUUID())
        Assertions.assertNotNull(eventEntity)
        Assertions.assertEquals(event.name, eventEntity.name)
        Assertions.assertEquals(event.description, eventEntity.description)
        Assertions.assertEquals(event.date, eventEntity.date)
        Assertions.assertEquals(event.totalSpots, eventEntity.totalSpots)
        Assertions.assertEquals(event.partnerId.value, eventEntity.partner.id.toString())
        Assertions.assertEquals(event.sections.size, eventEntity.sections.size)

        val sectionEntity = eventEntity.sections.first()
        Assertions.assertEquals(section.id.value, sectionEntity.id.toString())
        Assertions.assertEquals(event.id.value, sectionEntity.event.id.toString())
        Assertions.assertEquals(section.name, sectionEntity.name)
        Assertions.assertEquals(section.description, sectionEntity.description)
        Assertions.assertEquals(section.isPublished, sectionEntity.isPublished)
        Assertions.assertEquals(section.totalSpots, sectionEntity.totalSpots)
        Assertions.assertEquals(section.totalSpotsReserved.toLong(), sectionEntity.totalSpotsReserved)
        Assertions.assertEquals(section.price, sectionEntity.price)
        Assertions.assertEquals(section.spots.size, sectionEntity.spots.size)

        val spot = section.spots.first()
        val spotEntity = sectionEntity.spots.first { it.id.toString() == spot.id.value }
        Assertions.assertEquals(spot.id.value, spotEntity.id.toString())
        Assertions.assertEquals(section.id.value, spotEntity.eventSection.id.toString())
        Assertions.assertEquals(spot.location, spotEntity.location)
        Assertions.assertEquals(spot.isReserved, spotEntity.isReserved)
        Assertions.assertEquals(spot.isPublished, spotEntity.isPublished)
    }

    @Test
    @Transactional
    fun `should update an event`() {
        val partner = Partner.create(
            name = "Test Name"
        )

        val section = EventSection.create(
            name = "Section",
            description = "Some section description",
            totalSpots = 5L,
            price = 1000,
        )

        val event = partner.initializeEvent(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
            sections = mutableSetOf(section)
        )

        partnerRepository.add(partner)
        eventRepository.add(event)
        entityManager.flush()
        entityManager.clear()

        var eventEntity = entityManager.find(EventEntity::class.java, event.id.toUUID())
        Assertions.assertNotNull(eventEntity)
        Assertions.assertEquals(event.name, eventEntity.name)
        Assertions.assertEquals(event.description, eventEntity.description)
        Assertions.assertEquals(event.date, eventEntity.date)
        Assertions.assertEquals(event.totalSpots, eventEntity.totalSpots)
        Assertions.assertEquals(event.partnerId.value, eventEntity.partner.id.toString())
        Assertions.assertEquals(event.sections.size, eventEntity.sections.size)

        event.changeName("New Name")
        eventRepository.update(event)
        entityManager.flush()
        entityManager.clear()

        eventEntity = entityManager.find(EventEntity::class.java, event.id.toUUID())
        Assertions.assertNotNull(eventEntity)
        Assertions.assertEquals(event.name, eventEntity.name)
    }

    @Test
    @Transactional
    fun `should return a event when it is found`() {
        val partner = Partner.create(
            name = "Test Name"
        )

        val event = partner.initializeEvent(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
        )

        partnerRepository.add(partner)
        eventRepository.add(event)
        entityManager.flush()
        entityManager.clear()

        val savedEvent = eventRepository.findById(event.id)
        Assertions.assertNotNull(savedEvent)
        Assertions.assertEquals(event.id, savedEvent?.id)
        Assertions.assertEquals(event.name, savedEvent?.name)
    }

    @Test
    fun `should return null when event is no found`() {
        val savedEvent = eventRepository.findById(UUID.randomUUID().toDomainUuid<EventId>())
        Assertions.assertNull(savedEvent)
    }

    @Test
    @Transactional
    fun `should return a list of events`() {
        val partner = Partner.create(
            name = "Test Name"
        )

        val event1 = partner.initializeEvent(
            name = "Event Test One",
            description = "Event description",
            date = Instant.now(),
        )

        val event2 = partner.initializeEvent(
            name = "Event Test Two",
            description = "Event description",
            date = Instant.now(),
        )

        partnerRepository.add(partner)
        eventRepository.add(event1)
        eventRepository.add(event2)
        entityManager.flush()
        entityManager.clear()

        val events = eventRepository.findAll()

        Assertions.assertTrue(events.size == 2)
        Assertions.assertTrue(events.contains(event1))
        Assertions.assertTrue(events.contains(event2))
    }

    @Test
    @Transactional
    fun `should return an empty list of events`() {
        val events = eventRepository.findAll()
        Assertions.assertTrue(events.isEmpty())
    }

    @Test
    @Transactional
    fun `should delete a event when it is found`() {
        val partner = Partner.create(
            name = "Test Name"
        )

        val event = partner.initializeEvent(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
        )

        partnerRepository.add(partner)
        eventRepository.add(event)
        entityManager.flush()
        entityManager.clear()

        val savedEvent = entityManager.find(EventEntity::class.java, event.id.toUUID())
        Assertions.assertNotNull(savedEvent)
        Assertions.assertEquals(event.name, savedEvent?.name)

        eventRepository.delete(event.id)

        val deletedEvent = entityManager.find(EventEntity::class.java, event.id.toUUID())

        Assertions.assertNull(deletedEvent)
    }

    @Test
    fun `should do nothing when doesn't find a event to delete`() {
        Assertions.assertDoesNotThrow {
            eventRepository.delete(UUID.randomUUID().toDomainUuid<EventId>())
        }
    }
}
