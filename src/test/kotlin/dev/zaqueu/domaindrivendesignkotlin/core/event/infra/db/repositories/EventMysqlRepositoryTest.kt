package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
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
    private lateinit var eventRepository: EventRepository

    @Test
    @Transactional
    fun `should add a new event`() {
        val section = EventSection.create(
            name = "Section",
            description = "Some section description",
            totalSpots = 5L,
            price = 1000,
        )

        val event = Event.create(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
            partnerId = PartnerId().value,
            sections = mutableSetOf(section)
        )

        eventRepository.add(event)
        entityManager.flush()
        entityManager.clear()

        val eventEntity = entityManager.find(EventEntity::class.java, UUID.fromString(event.id.value))
        Assertions.assertNotNull(eventEntity)
        Assertions.assertEquals(event.name, eventEntity.name)
        Assertions.assertEquals(event.description, eventEntity.description)
        Assertions.assertEquals(event.date, eventEntity.date)
        Assertions.assertEquals(event.partnerId.value, eventEntity.partnerId.toString())
        Assertions.assertEquals(event.sections.size, eventEntity.sections.size)

        val sectionEntity = eventEntity.sections.first()
        Assertions.assertEquals(section.name, sectionEntity.name)
        Assertions.assertEquals(section.description, sectionEntity.description)
        Assertions.assertEquals(section.totalSpots, sectionEntity.totalSpots)
        Assertions.assertEquals(section.totalSpotsReserved.toLong(), sectionEntity.totalSpotsReserved)
        Assertions.assertEquals(section.price, sectionEntity.price)
        Assertions.assertEquals(section.spots.size, sectionEntity.spots.size)
    }

    @Test
    @Transactional
    fun `should return a event when it is found`() {
        val event = Event.create(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
            partnerId = PartnerId().value,
        )

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
        val savedEvent = eventRepository.findById(EventId(UUID.randomUUID().toString()))
        Assertions.assertNull(savedEvent)
    }

    @Test
    @Transactional
    fun `should return a list of events`() {
        val event1 = Event.create(
            name = "Event Test One",
            description = "Event description",
            date = Instant.now(),
            partnerId = PartnerId().value,
        )

        val event2 = Event.create(
            name = "Event Test Two",
            description = "Event description",
            date = Instant.now(),
            partnerId = PartnerId().value,
        )

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
        val event = Event.create(
            name = "Event Test",
            description = "Event description",
            date = Instant.now(),
            partnerId = PartnerId().value,
        )

        eventRepository.add(event)
        entityManager.flush()
        entityManager.clear()

        val savedEvent = entityManager.find(EventEntity::class.java, UUID.fromString(event.id.value))
        Assertions.assertNotNull(savedEvent)
        Assertions.assertEquals(event.name, savedEvent?.name)

        eventRepository.delete(event.id)

        val deletedEvent = entityManager.find(EventEntity::class.java, UUID.fromString(event.id.value))

        Assertions.assertNull(deletedEvent)
    }

    @Test
    fun `should do nothing when doesn't find a event to delete`() {
        Assertions.assertDoesNotThrow {
            eventRepository.delete(EventId(UUID.randomUUID().toString()))
        }
    }
}
