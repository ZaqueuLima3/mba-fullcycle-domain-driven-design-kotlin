package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import java.util.*

internal class PartnerId(value: String? = null) : Uuid(value)

internal fun String?.toPartnerId() = if (this != null) PartnerId(this) else PartnerId()

internal fun UUID.toPartnerId() = PartnerId(this.toString())
