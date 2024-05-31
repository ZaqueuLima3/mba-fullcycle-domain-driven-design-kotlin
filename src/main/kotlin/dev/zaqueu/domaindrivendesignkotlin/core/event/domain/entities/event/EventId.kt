package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal class EventId(value: String? = null) : Uuid(value)
