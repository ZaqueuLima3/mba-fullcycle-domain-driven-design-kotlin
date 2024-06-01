package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid

internal interface Repository<E : AggregateRoot> {
    fun add(entity: E)
    fun findById(id: Uuid): E?
    fun findAll(): List<E>
    fun delete(id: Uuid)
}
