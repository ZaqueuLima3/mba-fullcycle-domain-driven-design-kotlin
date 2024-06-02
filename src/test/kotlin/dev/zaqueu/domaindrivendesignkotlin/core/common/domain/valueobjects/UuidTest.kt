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

    @Test
    fun `should convert a string to a valid uuid vo`() {
        val id = UUID.randomUUID().toString()

        val mockId: MockId = id.toDomainUuid()

        Assertions.assertEquals(id, mockId.value)
    }

    @Test
    fun `should create a uuid vo from a null string`() {
        val id = null

        val mockId: MockId = id.toDomainUuid()

        Assertions.assertNotNull(mockId.value)

        Assertions.assertDoesNotThrow {
            UUID.fromString(mockId.value)
        }
    }
}
