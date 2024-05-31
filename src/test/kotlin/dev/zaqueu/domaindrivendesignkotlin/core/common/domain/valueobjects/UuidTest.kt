package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects

import dev.zaqueu.domaindrivendesignkotlin.core.event.exceptions.InvalidArgumentException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

internal class MockId(value: String? = null) : Uuid(value)

class UuidTest {
    @Test
    fun `should create a valid UUID from a string`() {
        val expectedUuid = UUID.randomUUID().toString()

        val uuid = MockId(expectedUuid)

        Assertions.assertEquals(expectedUuid, uuid.value)
    }

    @Test
    fun `should create a valid UUID without a value`() {
        val uuid = MockId()

        Assertions.assertNotNull(uuid.value)

        Assertions.assertDoesNotThrow {
            UUID.fromString(uuid.value)
        }
    }

    @Test
    fun `should throws a InvalidArgumentException when create a Uuid with a invalid value`() {
        val invalidUuid = "invalid-uuid"
        val expectedErrorMessage = "Value $invalidUuid must be a valid UUID"

        val actualException = Assertions.assertThrows(InvalidArgumentException::class.java) {
            MockId(invalidUuid)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }
}
