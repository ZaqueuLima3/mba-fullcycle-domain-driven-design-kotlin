package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.CreateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class EventServiceTest {
    @MockK
    internal lateinit var eventRepository: EventRepository

    @MockK
    internal lateinit var partnerRepository: PartnerRepository

    @MockK
    internal lateinit var unitOfWork: UnitOfWork

    private lateinit var eventService: EventService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        eventService = EventService(eventRepository, partnerRepository, unitOfWork)

        every {
            eventRepository.add(any())
        } just Runs

        every {
            unitOfWork.commit()
        } just Runs
    }

    @Test
    fun `should list all events`() {
        val expectedEvents = listOf(
            Event.create(
                name = "Event name",
                description = "some description",
                date = Instant.now(),
                partnerId = UUID.randomUUID().toString()
            )
        )

        every {
            eventRepository.findAll()
        } returns expectedEvents

        val events = eventService.list()

        Assertions.assertEquals(expectedEvents, events)

        verify {
            eventRepository.findAll()
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should list all event sections`() {
        val event = Event.create(
            name = "Event name",
            description = "some description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        event.addSection(
            name = "Section name",
            description = "Some description",
            totalSpots = 10,
            price = 1000,
        )

        every {
            eventRepository.findById(event.id)
        } returns event

        val sections = eventService.findSections(event.id.value)
        Assertions.assertTrue(sections.size == 1)

        verify {
            eventRepository.findById(event.id)
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should register a new event`() {
        val expectedPartnerId = UUID.randomUUID().toDomainUuid<PartnerId>()

        val partner = Partner(
            id = expectedPartnerId,
            name = "Disney",
        )

        val input = CreateEventDto(
            name = "Event name",
            description = "some description",
            date = Instant.now(),
            partnerId = expectedPartnerId.value,
        )

        every {
            partnerRepository.findById(partner.id)
        } returns partner

        val event = eventService.create(input)

        Assertions.assertNotNull(event.id)
        Assertions.assertEquals(input.name, event.name)
        Assertions.assertEquals(input.description, event.description)
        Assertions.assertEquals(input.date, event.date)
        Assertions.assertEquals(input.partnerId, event.partnerId.value)

        verify {
            eventRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(eventRepository, unitOfWork)
    }

    @Test
    fun `should update the event name`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateEventDto(
            id = expectedId,
            name = "Event name",
            description = "some description",
            date = Instant.now().plusMillis(1209600000),
            isPublished = true,
        )

        val event = Event(
            id = expectedId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(event.id)
        } returns event

        val updatedEvent = eventService.update(input)

        Assertions.assertEquals(input.name, updatedEvent.name)
        Assertions.assertEquals(input.description, updatedEvent.description)
        Assertions.assertEquals(input.date, updatedEvent.date)
        Assertions.assertEquals(input.isPublished, updatedEvent.isPublished)

        verify {
            eventRepository.findById(event.id)
            eventRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(eventRepository, unitOfWork)
    }

    @Test
    fun `should not update values to null`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateEventDto(
            id = expectedId,
        )

        val event = Event(
            id = expectedId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(event.id)
        } returns event

        val updatedEvent = eventService.update(input)

        Assertions.assertEquals(event.name, updatedEvent.name)
        Assertions.assertEquals(event.description, updatedEvent.description)
        Assertions.assertEquals(event.date, updatedEvent.date)
        Assertions.assertEquals(event.isPublished, updatedEvent.isPublished)

        verify {
            eventRepository.findById(event.id)
            eventRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(eventRepository, unitOfWork)
    }

    @Test
    fun `should publish the event`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateEventDto(
            id = expectedId,
            isPublished = true,
        )

        val event = Event(
            id = expectedId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = false,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(event.id)
        } returns event

        val updatedEvent = eventService.update(input)

        Assertions.assertTrue(updatedEvent.isPublished)
    }

    @Test
    fun `should unpublish the event`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateEventDto(
            id = expectedId,
            isPublished = false,
        )

        val event = Event(
            id = expectedId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(event.id)
        } returns event

        val updatedEvent = eventService.update(input)

        Assertions.assertFalse(updatedEvent.isPublished)
    }
}
