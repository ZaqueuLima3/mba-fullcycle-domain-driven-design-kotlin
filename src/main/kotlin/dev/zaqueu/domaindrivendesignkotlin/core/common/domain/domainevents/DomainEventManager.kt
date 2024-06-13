package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import org.springframework.stereotype.Component

@Component
internal class DomainEventManager(
    private val eventEmitter: EventEmitter
) {
    fun register(event: String, handler: (event: DomainEvent) -> Unit) {
        this.eventEmitter.on(event, handler)
    }

    fun publish(aggregateRoot: AggregateRoot) {
        for (event in aggregateRoot.events) {
            val name = event::class.simpleName ?: continue
            this.eventEmitter.emit(name, event)
        }
        aggregateRoot.clearEvents()
    }

    fun publish(vararg aggregateRoots: AggregateRoot) {
        for (aggregate in aggregateRoots) {
            publish(aggregate)
        }
    }
}
