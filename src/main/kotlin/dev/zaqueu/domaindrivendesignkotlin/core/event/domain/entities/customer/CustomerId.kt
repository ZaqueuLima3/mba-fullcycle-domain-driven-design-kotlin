package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.customer

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal class CustomerId(value: String? = null) : Uuid(value)
