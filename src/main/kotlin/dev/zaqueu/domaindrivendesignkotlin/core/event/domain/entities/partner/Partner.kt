package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.partner

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event.Event
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

    fun initializeEvent(name: String, description: String, date: Instant): Event {
        return Event.create(
            name = name,
            description = description,
            date = date,
            partnerId = id.value,
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
