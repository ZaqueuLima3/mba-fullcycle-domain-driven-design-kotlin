package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Entity
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId

internal data class EventSpot(
    override val id: EventSpotId,
    var location: String?,
    val isReserved: Boolean,
    var isPublished: Boolean,
) : Entity() {

    constructor(
        id: String? = null,
        location: String?,
        isPublished: Boolean,
        isReserved: Boolean,
    ) : this(
        id = if (id != null) EventSpotId(id) else EventSpotId(),
        location = location,
        isPublished = isPublished,
        isReserved = isReserved,
    )

    fun changeLocation(location: String) {
        this.location = location
    }

    fun publish() {
        this.isPublished = true
    }

    fun unPublish() {
        this.isPublished = false
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
