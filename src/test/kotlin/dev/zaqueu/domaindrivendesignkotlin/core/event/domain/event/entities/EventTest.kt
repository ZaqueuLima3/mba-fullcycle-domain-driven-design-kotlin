package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class EventTest {
    @Test
    fun `should create an Event`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        Assertions.assertNotNull(event.id)
        Assertions.assertEquals(expectedName, event.name)
        Assertions.assertEquals(expectedDescription, event.description)
        Assertions.assertEquals(expectedDate, event.date)
        Assertions.assertEquals(expectedPartnerId.value, event.partnerId.value)
        Assertions.assertFalse(event.isPublished)
        Assertions.assertEquals(0, event.totalSpots)
        Assertions.assertEquals(0, event.totalSpotsReserved)
        Assertions.assertEquals(0, event.sections.size)
    }

    @Test
    fun `should add a section to a new event`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L
        val expectedTotalOfSections = 1

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        event.addSection(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        Assertions.assertEquals(expectedTotalOfSections, event.sections.size)
        Assertions.assertEquals(expectedSectionTotalSpots, event.totalSpots)

        val section = event.sections.first()

        Assertions.assertEquals(expectedSectionName, section.name)
        Assertions.assertEquals(expectedSectionDescription, section.description)
        Assertions.assertEquals(expectedSectionTotalSpots, section.totalSpots)
        Assertions.assertEquals(expectedSectionPrice, section.price)
        Assertions.assertEquals(0, section.totalSpotsReserved)
        Assertions.assertFalse(section.isPublished)
        Assertions.assertEquals(expectedSectionTotalSpots.toInt(), section.spots.size)
    }

    @Test
    fun `should add sections to a new event`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L
        val expectedTotalOfSections = 1

        val section = EventSection.create(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        event.addSections(mutableSetOf(section))

        Assertions.assertEquals(expectedTotalOfSections, event.sections.size)
        Assertions.assertEquals(expectedSectionTotalSpots, event.totalSpots)

        Assertions.assertEquals(expectedSectionName, section.name)
        Assertions.assertEquals(expectedSectionDescription, section.description)
        Assertions.assertEquals(expectedSectionTotalSpots, section.totalSpots)
        Assertions.assertEquals(expectedSectionPrice, section.price)
        Assertions.assertEquals(0, section.totalSpotsReserved)
        Assertions.assertFalse(section.isPublished)
        Assertions.assertEquals(expectedSectionTotalSpots.toInt(), section.spots.size)
    }

    @Test
    fun `should change the event name`() {
        val expectedName = "First Event"
        val expectedUpdateName = "New Event Name"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        Assertions.assertEquals(expectedName, event.name)

        event.changeName(expectedUpdateName)

        Assertions.assertEquals(expectedUpdateName, event.name)
    }

    @Test
    fun `should change the event description`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedUpdateDescription = "New description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        Assertions.assertEquals(expectedDescription, event.description)

        event.changeDescription(expectedUpdateDescription)

        Assertions.assertEquals(expectedUpdateDescription, event.description)
    }

    @Test
    fun `should change the event date`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedUpdateDate = expectedDate.plusMillis(1209600000)
        val expectedPartnerId = PartnerId()

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        Assertions.assertEquals(expectedDate, event.date)

        event.changeDate(expectedUpdateDate)

        Assertions.assertEquals(expectedUpdateDate, event.date)
    }

    @Test
    fun `should publish and unPublish an event`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        Assertions.assertFalse(event.isPublished)

        event.publish()
        Assertions.assertTrue(event.isPublished)

        event.unPublish()
        Assertions.assertFalse(event.isPublished)
    }

    @Test
    fun `should publishAll and unPublishAll event section and spots`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val section1 = EventSection.create(
            name = "Section one",
            description = "Some section description",
            totalSpots = 5L,
            price = 1000,
        )

        val section2 = EventSection.create(
            name = "Section two",
            description = "Some section description",
            totalSpots = 5L,
            price = 1000,
        )

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        event.addSections(mutableSetOf(section1, section2))

        val sections = event.sections

        Assertions.assertFalse(event.isPublished)
        Assertions.assertFalse(sections.all { it.isPublished })
        Assertions.assertFalse(sections.all { s -> s.spots.all { it.isPublished } })

        event.publishAll()
        Assertions.assertTrue(event.isPublished)
        Assertions.assertTrue(sections.all { it.isPublished })
        Assertions.assertTrue(sections.all { s -> s.spots.all { it.isPublished } })

        event.unPublishAll()
        Assertions.assertFalse(event.isPublished)
        Assertions.assertFalse(sections.all { it.isPublished })
        Assertions.assertFalse(sections.all { s -> s.spots.all { it.isPublished } })
    }

    @Test
    fun `should change a section information`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L

        val expectedUpdateName = "Section Name"
        val expectedUpdateDescription = "New Description"
        val expectedUpdatePrice = 100L

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        event.addSection(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        event.changeSectionInformation(
            sectionId = event.sections.first().id,
            name = expectedUpdateName,
            description = expectedUpdateDescription,
            price = expectedUpdatePrice,
        )

        val section = event.sections.first()

        Assertions.assertEquals(expectedUpdateName, section.name)
        Assertions.assertEquals(expectedUpdateDescription, section.description)
        Assertions.assertEquals(expectedUpdatePrice, section.price)
    }

    @Test
    fun `should change a spot location`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 1L
        val expectedSectionPrice = 1000L

        val expectedUpdateLocation = "A1:11"

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        event.addSection(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        val section = event.sections.first()

        event.changeSpotLocation(
            sectionId = section.id,
            spotId = section.spots.first().id,
            location = expectedUpdateLocation,
        )

        val spot = section.spots.first()

        Assertions.assertEquals(expectedUpdateLocation, spot.location)
    }

    @Test
    fun `should return a section`() {
        val expectedName = "First Event"
        val expectedDescription = "some description"
        val expectedDate = Instant.now()
        val expectedPartnerId = PartnerId()

        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L

        val section = EventSection.create(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        val event = Event.create(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = expectedPartnerId.value,
        )

        event.addSections(setOf(section))

        val sectionFound = event.getSection(section.id)

        Assertions.assertEquals(section.id, sectionFound?.id)
    }

    @Test
    fun `should not allow reservation if the event is not published`() {
        val spot = EventSpot.create()

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 0,
            price = 1000,
        )
        section.addSpot(spot)

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )
        event.addSections(setOf(section))
        event.unPublishAll()

        val allowReservation = event.allowReserveSpot(section.id, spot.id)
        Assertions.assertFalse(allowReservation)
    }

    @Test
    fun `should not allow reservation if the section returns true`() {
        val spot = EventSpot.create()
        spot.reserve()

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 0,
            price = 1000,
        )
        section.addSpot(spot)

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )
        event.addSections(setOf(section))
        event.unPublishAll()

        val allowReservation = event.allowReserveSpot(section.id, spot.id)
        Assertions.assertFalse(allowReservation)
    }

    @Test
    fun `should allow reservation if the section returns true and it is published`() {
        val spot = EventSpot.create()

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 0,
            price = 1000,
        )
        section.addSpot(spot)

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )
        event.addSections(setOf(section))
        event.publishAll()

        val allowReservation = event.allowReserveSpot(section.id, spot.id)
        Assertions.assertTrue(allowReservation)
    }

    @Test
    fun `should throws an exception if section do not exist`() {
        val expectedErrorMessage = "Section not found"

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )
        event.publishAll()

        val actualException = Assertions.assertThrows(Exception::class.java) {
            event.allowReserveSpot(UUID.randomUUID().toDomainUuid(), UUID.randomUUID().toDomainUuid())
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should throws an exception when try to reserve a non existent section`() {
        val expectedErrorMessage = "Section not found"

        val spot = EventSpot.create()

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 10,
            price = 1000,
        )

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )
        event.publishAll()

        val actualException = Assertions.assertThrows(Exception::class.java) {
            event.reserveSpot(section, spot)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should does not throws when reserve an existent section`() {
        val spot = EventSpot.create()

        val section = EventSection.create(
            name = "name",
            description = "description",
            totalSpots = 0,
            price = 1000,
        )
        section.addSpot(spot)

        val event = Event.create(
            name = "name",
            description = "description",
            date = Instant.now(),
            partnerId = UUID.randomUUID().toString(),
        )
        event.addSections(setOf(section))
        event.publishAll()

        Assertions.assertDoesNotThrow {
            event.reserveSpot(section, spot)
        }
    }
}
