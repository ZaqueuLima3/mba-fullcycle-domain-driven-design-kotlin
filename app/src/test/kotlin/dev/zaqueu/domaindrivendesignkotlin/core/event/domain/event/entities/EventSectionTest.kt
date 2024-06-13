package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.ResourceNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class EventSectionTest {
    @Test
    fun `should change an event section name`() {
        val expectedSectionName = "Section one"
        val expectedUpdatedSectionName = "Section two"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L

        val section = EventSection.create(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        Assertions.assertEquals(expectedSectionName, section.name)

        section.changeName(expectedUpdatedSectionName)
        Assertions.assertEquals(expectedUpdatedSectionName, section.name)
    }

    @Test
    fun `should change an event section description`() {
        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedUpdatedSectionDescription = "New description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L

        val section = EventSection.create(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        Assertions.assertEquals(expectedSectionDescription, section.description)

        section.changeDescription(expectedUpdatedSectionDescription)
        Assertions.assertEquals(expectedUpdatedSectionDescription, section.description)
    }

    @Test
    fun `should change an event section price`() {
        val expectedSectionName = "Section one"
        val expectedSectionDescription = "Some section description"
        val expectedSectionTotalSpots = 10L
        val expectedSectionPrice = 1000L
        val expectedUpdatedSectionPrice = 1500L

        val section = EventSection.create(
            name = expectedSectionName,
            description = expectedSectionDescription,
            totalSpots = expectedSectionTotalSpots,
            price = expectedSectionPrice,
        )

        Assertions.assertEquals(expectedSectionPrice, section.price)

        section.changePrice(expectedUpdatedSectionPrice)
        Assertions.assertEquals(expectedUpdatedSectionPrice, section.price)
    }

    @Test
    fun `should publish and unPublish an event section`() {
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

        Assertions.assertFalse(section.isPublished)

        section.publish()
        Assertions.assertTrue(section.isPublished)

        section.unPublish()
        Assertions.assertFalse(section.isPublished)
    }

    @Test
    fun `should add a new spot`() {
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

        Assertions.assertEquals(expectedSectionTotalSpots, section.totalSpots)

        section.addSpot(EventSpot.create())
        Assertions.assertEquals(expectedSectionTotalSpots +1, section.totalSpots)
    }

    @Test
    fun `should add a list of spots`() {
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

        Assertions.assertEquals(expectedSectionTotalSpots, section.totalSpots)

        section.addSpots(mutableSetOf(EventSpot.create()))
        Assertions.assertEquals(expectedSectionTotalSpots +1, section.totalSpots)
    }

    @Test
    fun `should return a spot`() {
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
        val spot = EventSpot.create()

        section.addSpot(spot)

        val spotFound = section.getSpot(spot.id)

        Assertions.assertEquals(spot.id, spotFound?.id)
    }

    @Test
    fun `should not allow reservation if the section is not published`() {
        val spot = EventSpot.create()
        val section = EventSection.create(
            name = "section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )
        section.addSpot(spot)
        section.unPublish()

        val allowReservation = section.allowReserveSpot(spot.id)
        Assertions.assertFalse(allowReservation)
    }

    @Test
    fun `should not allow reservation if the spot returns false`() {
        val spot = EventSpot.create()
        spot.reserve()
        val section = EventSection.create(
            name = "section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )
        section.addSpot(spot)
        section.publish()

        val allowReservation = section.allowReserveSpot(spot.id)
        Assertions.assertFalse(allowReservation)
    }

    @Test
    fun `should allow reservation if the spot returns true and it is published`() {
        val spot = EventSpot.create()
        val section = EventSection.create(
            name = "section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )
        section.addSpot(spot)
        section.publishAll()

        val allowReservation = section.allowReserveSpot(spot.id)
        Assertions.assertTrue(allowReservation)
    }

    @Test
    fun `should throws an exception if spot do not exist`() {
        val expectedSpotId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Spot with id $expectedSpotId not found"

        val section = EventSection.create(
            name = "section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )
        section.publish()

        val actualException = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            section.allowReserveSpot(expectedSpotId.toDomainUuid())
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should throws an exception when try to reserve a non existent spot`() {
        val expectedSpotId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Spot with id $expectedSpotId not found"

        val section = EventSection.create(
            name = "section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )
        section.publish()

        val actualException = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            section.reserveSpot(expectedSpotId.toDomainUuid())
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should does not throws when reserve an existent section`() {
        val spot = EventSpot.create()
        val section = EventSection.create(
            name = "section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )
        section.addSpot(spot)
        section.publishAll()

        Assertions.assertDoesNotThrow {
            section.reserveSpot(spot.id)
        }
    }
}
