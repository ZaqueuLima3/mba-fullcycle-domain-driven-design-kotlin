package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EventSpotTest {
    @Test
    fun `should reserve a spot`() {
        val spot = EventSpot.create()

        Assertions.assertFalse(spot.isReserved)

        spot.reserve()
        Assertions.assertTrue(spot.isReserved)
    }

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

    @Test
    fun `should not allow reservation if the spot is reserved`() {
        val spot = EventSpot.create()
        spot.reserve()

        Assertions.assertFalse(spot.allowReserveSpot())
    }

    @Test
    fun `should not allow reservation if the spot not published`() {
        val spot = EventSpot.create()
        spot.unPublish()

        Assertions.assertFalse(spot.allowReserveSpot())
    }

    @Test
    fun `should allow reservation if the spot is published and not reserved`() {
        val spot = EventSpot.create()
        spot.publish()

        Assertions.assertTrue(spot.allowReserveSpot())
    }
}
