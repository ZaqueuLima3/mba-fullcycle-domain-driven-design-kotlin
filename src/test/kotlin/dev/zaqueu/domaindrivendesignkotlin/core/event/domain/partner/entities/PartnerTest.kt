package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.domainevents.partner.PartnerChangedNameEvent
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.domainevents.partner.PartnerCreatedEvent
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class PartnerTest {
    @Test
    fun `should create a Partner`() {
        val expectedName = "Disney"

        val partner = Partner.create(
            name = expectedName,
        )

        Assertions.assertNotNull(partner.id)
        Assertions.assertDoesNotThrow { partner.id.toUUID() }
        Assertions.assertInstanceOf(PartnerId::class.java, partner.id)
        Assertions.assertEquals(expectedName, partner.name)
    }

    @Test
    fun `should create a Partner with an id`() {
        val expectedId = UUID.randomUUID().toString()
        val expectedName = "Disney"

        val partner = Partner(
            id = expectedId,
            name = expectedName,
        )

        Assertions.assertEquals(expectedId, partner.id.value)
        Assertions.assertEquals(expectedName, partner.name)
    }

    @Test
    fun `should create a Partner with a PartnerId`() {
        val expectedId = UUID.randomUUID().toDomainUuid<PartnerId>()
        val expectedName = "Disney"

        val partner = Partner(
            id = expectedId,
            name = expectedName,
        )

        Assertions.assertEquals(expectedId.value, partner.id.value)
        Assertions.assertEquals(expectedName, partner.name)
    }

    @Test
    fun `should register a domain event when a domain command occurred`() {
        var expectedName = "Disney"

        val partner = Partner.create(
            name = expectedName,
        )

        val eventCreated = partner.events.first { it is PartnerCreatedEvent } as PartnerCreatedEvent
        Assertions.assertTrue(partner.events.size == 1)
        Assertions.assertEquals(partner.id, eventCreated.aggregateRootId)
        Assertions.assertEquals(expectedName, eventCreated.name)

        expectedName = "Disney plus"
        partner.changeName(expectedName)

        val eventChanged = partner.events.first { it is PartnerChangedNameEvent } as PartnerChangedNameEvent
        Assertions.assertTrue(partner.events.size == 2)
        Assertions.assertEquals(partner.id, eventChanged.aggregateRootId)
        Assertions.assertEquals(expectedName, eventChanged.name)
    }

    @Test
    fun `should create an event`() {
        val expectedName = "Disney"
        val expectedEventName = "First Event"
        val expectedEventDescription = "Some event description"
        val expectedEventDate = Instant.now()

        val partner = Partner.create(
            name = expectedName,
        )

        val event = partner.initializeEvent(
            name = expectedEventName,
            description = expectedEventDescription,
            date = expectedEventDate,
        )

        Assertions.assertEquals(partner.id.value, event.partnerId.value)
        Assertions.assertEquals(expectedEventName, event.name)
        Assertions.assertEquals(expectedEventDescription, event.description)
        Assertions.assertEquals(expectedEventDate, event.date)
    }

    @Test
    fun `should change the partner name`() {
        val expectedName = "Disney"
        val expectedUpdateName = "Disney Plus"

        val partner = Partner.create(
            name = expectedName,
        )

        Assertions.assertEquals(expectedName, partner.name)

        partner.changeName(expectedUpdateName)

        Assertions.assertEquals(expectedUpdateName, partner.name)
    }
}
