package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSectionId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
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
    ) : this(
        id = id.toDomainUuid(),
        name = name,
        description = description,
        date = date,
        isPublished = isPublished,
        totalSpotsReserved = totalSpotsReserved,
        partnerId = partnerId.toDomainUuid(),
    )

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

    fun addSections(sections: Set<EventSection>) {
        this._sections.addAll(sections)
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

    fun getSection(sectionId: EventSectionId): EventSection? {
        return this.sections.firstOrNull { it.id == sectionId }
    }

    fun changeSectionInformation(
        sectionId: EventSectionId,
        name: String?,
        description: String?,
        price: Long?,
    ) {
        val section = getSection(sectionId)
            ?: throw Exception("Section not found")

        if (name != null) section.changeName(name)
        if (description != null) section.changeDescription(description)
        if (price != null) section.changePrice(price)
    }

    fun changeSpotLocation(
        sectionId: EventSectionId,
        spotId: EventSpotId,
        location: String,
    ) {
        val section = getSection(sectionId)
            ?: throw Exception("Section not found")

        section.changeSpotLocation(spotId, location)
    }

    fun allowReserveSpot(
        sectionId: EventSectionId,
        spotId: EventSpotId,
    ): Boolean {
        if (!this.isPublished) return false

        val section = getSection(sectionId)
            ?: throw Exception("Section not found")

        return section.allowReserveSpot(spotId)
    }

    fun reserveSpot(section: EventSection, spot: EventSpot) {
        val sectionFound = getSection(section.id)
            ?: throw Exception("Section not found")

        sectionFound.reserveSpot(spot.id)
    }

    companion object {
        fun create(
            name: String,
            description: String?,
            date: Instant,
            partnerId: String,
        ): Event {
            return Event(
                name = name,
                description = description,
                date = date,
                isPublished = false,
                totalSpotsReserved = 0,
                partnerId = partnerId,
            )
        }
    }
}
