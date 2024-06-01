package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
}
