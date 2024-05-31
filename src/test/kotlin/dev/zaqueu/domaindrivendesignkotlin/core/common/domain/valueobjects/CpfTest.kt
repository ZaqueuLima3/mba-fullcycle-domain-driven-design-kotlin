package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects

import dev.zaqueu.domaindrivendesignkotlin.core.event.exceptions.InvalidArgumentException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CpfTest {
    @Test
    fun `should create a valid Cpf`() {
        val expectedCpf = "93928642057"

        val cpf = Cpf.create(expectedCpf)

        Assertions.assertEquals(expectedCpf, cpf.value)
    }

    @Test
    fun `should remove empty spaces`() {
        val expectedCpf = "93928642057"

        val cpf = Cpf.create("939 286 420 57")

        Assertions.assertEquals(expectedCpf, cpf.value)
    }

    @Test
    fun `should throws an InvalidArgumentException when Cpf is too short`() {
        val invalidCpf = "9392"
        val expectedErrorMessage = "CPF must have 11 digits, but has ${invalidCpf.length}"

        val actualException = Assertions.assertThrows(InvalidArgumentException::class.java) {
            Cpf.create(invalidCpf)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should throws an InvalidArgumentException when Cpf digits is equal`() {
        val invalidCpf = "11111111111"
        val expectedErrorMessage = "Cpf must have at least two different digits"

        val actualException = Assertions.assertThrows(InvalidArgumentException::class.java) {
            Cpf.create(invalidCpf)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should throws an InvalidArgumentException when Cpf first digit is not valid`() {
        val invalidCpf = "93928642027"
        val expectedErrorMessage = "Invalid Cpf"

        val actualException = Assertions.assertThrows(InvalidArgumentException::class.java) {
            Cpf.create(invalidCpf)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `should throws an InvalidArgumentException when Cpf second digit is not valid`() {
        val invalidCpf = "93928642051"
        val expectedErrorMessage = "Invalid Cpf"

        val actualException = Assertions.assertThrows(InvalidArgumentException::class.java) {
            Cpf.create(invalidCpf)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }
}
