package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import java.util.UUID

internal class EventId(value: String? = null) : Uuid(value)

internal fun String?.toEventId() = if (this != null) EventId(this) else EventId()

internal fun UUID.toEventId() = EventId(this.toString())
