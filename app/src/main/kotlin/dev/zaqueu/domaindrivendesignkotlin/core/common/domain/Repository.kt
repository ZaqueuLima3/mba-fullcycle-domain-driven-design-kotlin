package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import org.springframework.transaction.annotation.Transactional

internal interface Repository<E : AggregateRoot> {
    fun add(entity: E)
    fun update(entity: E)
    fun findById(id: Uuid): E?
    fun findAll(): List<E>
    fun delete(id: Uuid)
    @Transactional
    fun deleteAll()
}
