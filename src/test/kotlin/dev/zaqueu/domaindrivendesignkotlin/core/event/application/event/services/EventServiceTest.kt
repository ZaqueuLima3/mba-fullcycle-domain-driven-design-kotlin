package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.*
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

    private lateinit var eventService: EventService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        eventService = EventService(eventRepository, partnerRepository)

        every {
            eventRepository.add(any())
        } just Runs

        every {
            eventRepository.update(any())
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
    fun `should list all event's sections`() {
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
    fun `should throws an exception when try to list sections of a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.findSections(UUID.randomUUID().toString())
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should list all section's spots`() {
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

        val section = event.sections.first()

        every {
            eventRepository.findById(event.id)
        } returns event

        val spots = eventService.findSpots(event.id.value, section.id.value)
        Assertions.assertTrue(spots.size == 10)

        verifySequence {
            eventRepository.findById(event.id)
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to list spots of a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.findSpots(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should throws an exception when try to list spots of a non existent section`() {
        val expectedMessage = "Section not found"

        val event = Event.create(
            name = "Event",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(any())
        } returns event

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.findSpots(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
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

        verifySequence {
            eventRepository.add(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to create a event with non existent partner`() {
        val expectedMessage = "Partner not found"

        every {
            partnerRepository.findById(any())
        } returns null

        val input = CreateEventDto(
            name = "Event",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.create(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should add a section to a created event`() {
        val event = Event.create(
            name = "Event name",
            description = "some description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString()
        )

        val sectionDto = CreateEventSectionDto(
            eventId = event.id.value,
            name = "Section name",
            description = "Some description",
            totalSpots = 10,
            price = 1000L,
        )

        every {
            eventRepository.findById(event.id)
        } returns event

        Assertions.assertTrue(event.sections.isEmpty())

        val eventUpdated = eventService.addSection(sectionDto)

        Assertions.assertTrue(eventUpdated.sections.size == 1)

        val section = eventUpdated.sections.first()
        Assertions.assertEquals(sectionDto.name, section.name)
        Assertions.assertEquals(sectionDto.description, section.description)

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.add(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to add a section on a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val input = CreateEventSectionDto(
            eventId = UUID.randomUUID().toString(),
            name = "Section",
            description = "description",
            totalSpots = 10,
            price = 1000,
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.addSection(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should update the event name`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdateEventDto(
            id = expectedId,
            name = "Event name",
            description = "some description",
            date = Instant.now().plusMillis(1209600000),
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

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
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

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to update a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val input = UpdateEventDto(
            id = UUID.randomUUID().toString(),
            name = "Section",
            description = "description",
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.update(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should update the event section`() {
        val expectedEventId = UUID.randomUUID().toString()
        val event = Event(
            id = expectedEventId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        event.addSection(
            name = "Section",
            description = "description",
            totalSpots = 2,
            price = 1000,
        )

        val expectedEventSectionId = event.sections.first().id.value

        val updateEventSectionDto = UpdateEventSectionDto(
            eventId = expectedEventId,
            sectionId = expectedEventSectionId,
            name = "Updated section name",
            description = "updated description",
            price = 100,
        )

        every {
            eventRepository.findById(any())
        } returns event

        val updateEvent = eventService.updateEventSection(updateEventSectionDto)
        val section = updateEvent.sections.find { it.id.value == expectedEventSectionId }

        Assertions.assertNotNull(section)
        Assertions.assertEquals(updateEventSectionDto.name, section?.name)
        Assertions.assertEquals(updateEventSectionDto.description, section?.description)
        Assertions.assertEquals(updateEventSectionDto.price, section?.price)

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should update the event section values to null`() {
        val expectedEventId = UUID.randomUUID().toString()
        val event = Event(
            id = expectedEventId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        event.addSection(
            name = "Section",
            description = "description",
            totalSpots = 2,
            price = 1000,
        )

        val section = event.sections.first()

        val updateEventSectionDto = UpdateEventSectionDto(
            eventId = expectedEventId,
            sectionId = section.id.value,
        )

        every {
            eventRepository.findById(any())
        } returns event

        val updateEvent = eventService.updateEventSection(updateEventSectionDto)
        val updatedSection = updateEvent.sections.find { it.id.value == section.id.value }

        Assertions.assertNotNull(section)
        Assertions.assertEquals(section.name, updatedSection?.name)
        Assertions.assertEquals(section.description, updatedSection?.description)
        Assertions.assertEquals(section.price, updatedSection?.price)

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to update a section of a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val input = UpdateEventSectionDto(
            eventId = UUID.randomUUID().toString(),
            sectionId = UUID.randomUUID().toString(),
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.updateEventSection(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should throws an exception when try to update a non existent section`() {
        val expectedMessage = "Section not found"

        val event = Event(
            id = UUID.randomUUID().toString(),
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(any())
        } returns event

        val input = UpdateEventSectionDto(
            eventId = UUID.randomUUID().toString(),
            sectionId = UUID.randomUUID().toString(),
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.updateEventSection(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should update the section spot location`() {
        val expectedEventId = UUID.randomUUID().toString()
        val event = Event(
            id = expectedEventId,
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        event.addSection(
            name = "Section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        val section = event.sections.first()
        val spot = section.spots.first()

        val updateEventSpotLocationDto = UpdateEventSpotLocationDto(
            eventId = expectedEventId,
            sectionId = section.id.value,
            spotId = spot.id.value,
            location = "A1:11",
        )

        every {
            eventRepository.findById(any())
        } returns event

        val updateEvent = eventService.updateEventSpotLocation(updateEventSpotLocationDto)
        val updatedSection = updateEvent.sections.find { it.id == section.id }
        val updatedSpot = updatedSection?.spots?.find { it.id == spot.id }

        Assertions.assertNotNull(updatedSpot)
        Assertions.assertEquals(updateEventSpotLocationDto.location, updatedSpot?.location)

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to update a spot location of a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val input = UpdateEventSpotLocationDto(
            eventId = UUID.randomUUID().toString(),
            sectionId = UUID.randomUUID().toString(),
            spotId = UUID.randomUUID().toString(),
            location = "A1:11"
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.updateEventSpotLocation(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should throws an exception when try to update a spot location of a non existent section`() {
        val expectedMessage = "Section not found"

        val event = Event(
            id = UUID.randomUUID().toString(),
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        every {
            eventRepository.findById(any())
        } returns event

        val input = UpdateEventSpotLocationDto(
            eventId = event.id.value,
            sectionId = UUID.randomUUID().toString(),
            spotId = UUID.randomUUID().toString(),
            location = "A1:11"
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.updateEventSpotLocation(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should throws an exception when try to update a location of a non existent spot`() {
        val expectedMessage = "Spot not found"

        val event = Event(
            id = UUID.randomUUID().toString(),
            name = "Event",
            description = "description",
            date = Instant.now(),
            isPublished = true,
            totalSpotsReserved = 0,
            partnerId = UUID.randomUUID().toString()
        )

        event.addSection(
            name = "Section",
            description = "desctiption",
            totalSpots = 0,
            price = 100,
        )

        every {
            eventRepository.findById(any())
        } returns event

        val input = UpdateEventSpotLocationDto(
            eventId = event.id.value,
            sectionId = event.sections.first().id.value,
            spotId = UUID.randomUUID().toString(),
            location = "A1:11"
        )

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.updateEventSpotLocation(input)
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should publish the event`() {
        val expectedId = UUID.randomUUID().toString()

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

        val updatedEvent = eventService.publishAll(expectedId)

        Assertions.assertTrue(updatedEvent.isPublished)

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to publish a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.publishAll(UUID.randomUUID().toString())
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }

    @Test
    fun `should unPublish the event`() {
        val expectedId = UUID.randomUUID().toString()

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

        val updatedEvent = eventService.unPublishAll(expectedId)

        Assertions.assertFalse(updatedEvent.isPublished)

        verifySequence {
            eventRepository.findById(event.id)
            eventRepository.update(any())
        }
        confirmVerified(eventRepository)
    }

    @Test
    fun `should throws an exception when try to unPublish a non existent event`() {
        val expectedMessage = "Event not found"

        every {
            eventRepository.findById(any())
        } returns null

        val actualException = Assertions.assertThrows(Exception::class.java) {
            eventService.unPublishAll(UUID.randomUUID().toString())
        }

        Assertions.assertEquals(expectedMessage, actualException.message)
    }
}
