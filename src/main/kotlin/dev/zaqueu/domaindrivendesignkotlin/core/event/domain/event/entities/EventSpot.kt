package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Entity
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId

internal data class EventSpot(
    override val id: EventSpotId,
    var location: String?,
    var isReserved: Boolean,
    var isPublished: Boolean,
) : Entity() {

    constructor(
        id: String? = null,
        location: String?,
        isPublished: Boolean,
        isReserved: Boolean,
    ) : this(
        id = id.toDomainUuid(),
        location = location,
        isPublished = isPublished,
        isReserved = isReserved,
    )

    fun changeLocation(location: String) {
        this.location = location
    }

    fun reserve() {
        this.isReserved = true
    }

    fun publish() {
        this.isPublished = true
    }

    fun unPublish() {
        this.isPublished = false
    }

    fun allowReserveSpot(): Boolean {
        return !this.isReserved && this.isPublished
    }

    companion object {
        fun create(): EventSpot {
            return EventSpot(
                location = null,
                isPublished = false,
                isReserved = false,
            )
        }
    }
}
