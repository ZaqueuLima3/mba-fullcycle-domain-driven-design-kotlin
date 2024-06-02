package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Entity
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSectionId

internal class EventSection(
    override val id: EventSectionId,
    var name: String,
    var description: String?,
    var isPublished: Boolean,
    var totalSpots: Long,
    var price: Long,
) : Entity() {
    private val _spots: MutableSet<EventSpot> = mutableSetOf()
    val spots: Set<EventSpot>
        get() = _spots.toSet()

    val totalSpotsReserved: Int
        get() = spots.filter { it.isReserved }.size

    constructor(
        id: String? = null,
        name: String,
        description: String?,
        isPublished: Boolean,
        totalSpots: Long,
        price: Long,
    ) : this(
        id = id.toDomainUuid(),
        name = name,
        description = description,
        isPublished = isPublished,
        totalSpots = totalSpots,
        price = price,
    ) {
        initializeSpots()
    }

    fun addSpots(spots: Set<EventSpot>) {
        _spots.addAll(spots)
        totalSpots = _spots.size.toLong()
    }

    fun addSpot(spot: EventSpot) {
        _spots.add(spot)
        totalSpots += 1
    }

    private fun initializeSpots() {
        for (i in 1..totalSpots) {
            _spots.add(EventSpot.create())
        }
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeDescription(description: String?) {
        this.description = description
    }

    fun changePrice(price: Long) {
        this.price = price
    }

    fun publishAll() {
        this.publish()
        this.spots.forEach { it.publish() }
    }

    fun unPublishAll() {
        this.unPublish()
        this.spots.forEach { it.unPublish() }
    }

    fun publish() {
        this.isPublished = true
    }

    fun unPublish() {
        this.isPublished = false
    }

    companion object {
        fun create(
            name: String,
            description: String?,
            totalSpots: Long,
            price: Long
        ): EventSection {
            return EventSection(
                name = name,
                description = description,
                isPublished = false,
                totalSpots = totalSpots,
                price = price,
            )
        }
    }
}
