package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal abstract class Entity {
    abstract val id: Uuid

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Entity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
