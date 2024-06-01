package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import java.time.Instant

internal data class Partner(
    override val id: PartnerId,
    var name: String,
) : AggregateRoot() {

    constructor(
        id: String? = null,
        name: String,
    ) : this(
        id = if (id != null) PartnerId(id) else PartnerId(),
        name = name,
    )

    fun initializeEvent(name: String, description: String, date: Instant, sections: Set<EventSection> = emptySet()): Event {
        return Event.create(
            name = name,
            description = description,
            date = date,
            partnerId = id.value,
            sections = sections,
        )
    }

    fun changeName(name: String) {
        this.name = name
    }

    companion object {
        fun create(name: String): Partner {
            return Partner(name = name)
        }
    }
}
