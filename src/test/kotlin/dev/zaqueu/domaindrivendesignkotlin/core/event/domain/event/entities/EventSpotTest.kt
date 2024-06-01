package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EventSpotTest {
    @Test
    fun `should change the event spot location`() {
        val expectedSpotLocation = "A:12"

        val spot = EventSpot.create()

        Assertions.assertNull(spot.location)

        spot.changeLocation(expectedSpotLocation)
        Assertions.assertEquals(expectedSpotLocation, spot.location)
    }

    @Test
    fun `should publish and unPublish an event spot`() {
        val spot = EventSpot.create()

        Assertions.assertFalse(spot.isPublished)

        spot.publish()
        Assertions.assertTrue(spot.isPublished)

        spot.unPublish()
        Assertions.assertFalse(spot.isPublished)
    }
}
