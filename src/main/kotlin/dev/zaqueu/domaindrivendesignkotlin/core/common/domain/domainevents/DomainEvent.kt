package dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import java.time.Instant

internal interface DomainEvent {
    val aggregateRootId: Uuid
    val occurredOn: Instant
    val eventVersion: Int
}
