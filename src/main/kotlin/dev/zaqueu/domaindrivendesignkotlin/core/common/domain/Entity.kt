package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal abstract class Entity {
    abstract val id: Uuid
}
