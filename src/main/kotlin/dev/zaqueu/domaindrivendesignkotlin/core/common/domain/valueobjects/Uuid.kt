package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.ValueObject
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.InvalidArgumentException
import java.util.*
import kotlin.reflect.full.primaryConstructor

internal abstract class Uuid(value: String?) : ValueObject<String>(value ?: UUID.randomUUID().toString()) {

    init {
        validate()
    }

    fun toUUID(): UUID = UUID.fromString(this.value)

    override fun toString(): String {
        return this.value
    }

    private fun validate() {
        try {
            UUID.fromString(value)
        } catch (e: Exception) {
            throw InvalidArgumentException("Value $value must be a valid UUID")
        }
    }
}

internal inline fun <reified T : Uuid> UUID.toDomainUuid(): T = mountClass(this.toString())

internal inline fun <reified T : Uuid> String?.toDomainUuid(): T = mountClass(this)

private inline fun <reified T : Uuid> mountClass(value: String?): T {
    val constructor = T::class.primaryConstructor ?: T::class.constructors.first()

    return try {
        constructor.call(value)
    } catch (e: Exception) {
        throw IllegalArgumentException("Failed to create instance of ${T::class} with value '$value'", e)
    }
}
