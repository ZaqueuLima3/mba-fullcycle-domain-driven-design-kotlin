package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal class EventId(value: String? = null) : Uuid(value)
