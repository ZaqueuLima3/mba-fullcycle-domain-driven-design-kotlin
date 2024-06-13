package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.valueobject

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal class OrderId(value: String? = null) : Uuid(value)