package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.domainevents.partner

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents.DomainEvent
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import java.time.Instant


internal class PartnerCreatedEvent(
    override val aggregateRootId: PartnerId,
    val name: String,
) : DomainEvent {
    override val eventVersion: Int = 1
    override val occurredOn: Instant = Instant.now()

    constructor(
        aggregateRootId: String, name: String,
    ) : this(
        aggregateRootId.toDomainUuid<PartnerId>(),
        name = name,
    )
}
