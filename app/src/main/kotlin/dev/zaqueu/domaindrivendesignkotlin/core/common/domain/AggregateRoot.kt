package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents.DomainEvent

internal abstract class AggregateRoot : Entity() {
    val events: MutableSet<DomainEvent> = mutableSetOf()

    fun registerDomainEvent(event: DomainEvent) {
        this.events.add(event)
    }

    fun clearEvents() {
        this.events.clear()
    }
}
