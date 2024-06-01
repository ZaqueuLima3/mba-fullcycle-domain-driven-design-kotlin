package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import java.time.Instant

internal class Event(
    override val id: EventId,
    var name: String,
    var description: String?,
    var date: Instant,
    var isPublished: Boolean,
    val totalSpotsReserved: Long,
    val partnerId: PartnerId,
) : AggregateRoot() {
    private val _sections: MutableSet<EventSection> = mutableSetOf()
    val sections: Set<EventSection>
        get() = _sections.toSet()
    val totalSpots: Long
        get() = sections.sumOf { it.totalSpots }

    constructor(
        id: String? = null,
        name: String,
        description: String?,
        date: Instant,
        isPublished: Boolean,
        totalSpotsReserved: Long,
        partnerId: String,
        sections: Set<EventSection>,
    ) : this(
        id = if (id != null) EventId(id) else EventId(),
        name = name,
        description = description,
        date = date,
        isPublished = isPublished,
        totalSpotsReserved = totalSpotsReserved,
        partnerId = PartnerId(partnerId),
    ) {
        this._sections.addAll(sections)
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeDescription(description: String?) {
        this.description = description
    }

    fun changeDate(date: Instant) {
        this.date = date
    }

    fun publishAll() {
        this.publish()
        this.sections.forEach { it.publishAll() }
    }

    fun unPublishAll() {
        this.unPublish()
        this.sections.forEach { it.unPublishAll() }
    }

    fun publish() {
        this.isPublished = true
    }

    fun unPublish() {
        this.isPublished = false
    }

    fun addSection(
        name: String,
        description: String?,
        totalSpots: Long,
        price: Long,
    ) {
        val section = EventSection.create(
            name = name,
            description = description,
            totalSpots = totalSpots,
            price = price,
        )

        _sections.add(section)
    }

    companion object {
        fun create(
            name: String,
            description: String?,
            date: Instant,
            partnerId: String,
            sections: Set<EventSection> = emptySet(),
        ): Event {
            return Event(
                name = name,
                description = description,
                date = date,
                isPublished = false,
                totalSpotsReserved = 0,
                partnerId = partnerId,
                sections = sections,
            )
        }
    }
}
