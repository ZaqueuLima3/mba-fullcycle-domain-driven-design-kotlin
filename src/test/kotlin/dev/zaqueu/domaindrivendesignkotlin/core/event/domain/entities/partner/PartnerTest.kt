package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.partner

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class PartnerTest {
    @Test
    fun `should create a Partner`() {
        val expectedName = "Disney"

        val partner = Partner.create(
            name = expectedName,
        )

        Assertions.assertNotNull(partner.id)
        Assertions.assertDoesNotThrow { UUID.fromString(partner.id.value) }
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
        val expectedId = PartnerId(UUID.randomUUID().toString())
        val expectedName = "Disney"

        val partner = Partner(
            id = expectedId,
            name = expectedName,
        )

        Assertions.assertEquals(expectedId.value, partner.id.value)
        Assertions.assertEquals(expectedName, partner.name)
    }
}
