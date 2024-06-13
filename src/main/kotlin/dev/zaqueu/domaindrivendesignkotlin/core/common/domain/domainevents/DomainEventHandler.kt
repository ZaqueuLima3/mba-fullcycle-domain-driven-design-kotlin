package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents

internal interface DomainEventHandler {
    fun handle(event: DomainEvent): Unit
}
