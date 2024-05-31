package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.ValueObject
import dev.zaqueu.domaindrivendesignkotlin.core.event.exceptions.InvalidArgumentException
import java.util.*

internal abstract class Uuid(value: String?) : ValueObject<String>(value ?: UUID.randomUUID().toString()) {

    init {
        validate()
    }

    private fun validate() {
        try {
            UUID.fromString(value)
        } catch (e: Exception) {
            throw InvalidArgumentException("Value $value must be a valid UUID")
        }
    }
}
