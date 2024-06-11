package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.ValueObject
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.InvalidArgumentException

internal class Cpf private constructor(value: String) : ValueObject<String>(value) {

    init {
        validate()
    }

    private fun validate() {
        if (value.length != 11) {
            throw InvalidArgumentException("CPF must have 11 digits, but has ${value.length}")
        }

        if (value.all { it == value[0] }) {
            throw InvalidArgumentException("Cpf must have at least two different digits")
        }

        val firstNineDigits = value.substring(0, 9)
        val firstCheckDigit = calculateCheckDigit(firstNineDigits)

        if (value[9].toString() != firstCheckDigit.toString()) {
            throw InvalidArgumentException("Invalid Cpf")
        }

        val firstTenDigits = value.substring(0, 10)
        val secondCheckDigit = calculateCheckDigit(firstTenDigits)

        if (value[10].toString() != secondCheckDigit.toString()) {
            throw InvalidArgumentException("Invalid Cpf")
        }
    }

    private fun calculateCheckDigit(digits: String): Int {
        val weights = digits.length + 1 downTo 2
        val sum = digits.map { it.toString().toInt() }
            .zip(weights)
            .sumOf { (digit, weight) -> digit * weight }

        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    companion object {
        fun create(value: String): Cpf {
            val regex = Regex("\\D")

            return Cpf(regex.replace(value, ""))
        }
    }
}
