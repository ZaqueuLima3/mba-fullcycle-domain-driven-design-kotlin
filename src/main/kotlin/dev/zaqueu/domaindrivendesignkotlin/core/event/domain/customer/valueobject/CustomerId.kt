package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import java.util.*

internal class CustomerId(value: String? = null) : Uuid(value)

internal fun String?.toCustomerId() = if (this != null) CustomerId(this) else CustomerId()

internal fun UUID.toCustomerId() = CustomerId(this.toString())
