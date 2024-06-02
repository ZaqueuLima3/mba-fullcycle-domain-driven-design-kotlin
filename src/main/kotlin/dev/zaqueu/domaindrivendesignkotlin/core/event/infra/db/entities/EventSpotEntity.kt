package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
import jakarta.persistence.*
import java.util.*

@Entity(name = "event_spots")
internal class EventSpotEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "location")
    var location: String?,

    @Column(name = "is_reserved")
    var isReserved: Boolean,

    @Column(name = "is_published")
    var isPublished: Boolean,

    @ManyToOne
    @JoinColumn(name = "event_section_id")
    var eventSection: EventSectionEntity
) {
    companion object {
        fun fromDomain(eventSpot: EventSpot, eventSection: EventSectionEntity): EventSpotEntity {
            return EventSpotEntity(
                id = eventSpot.id.toUUID(),
                location = eventSpot.location,
                isReserved = eventSpot.isReserved,
                isPublished = eventSpot.isPublished,
                eventSection = eventSection,
            )
        }

        fun EventSpotEntity.toDomain(): EventSpot {
            return EventSpot(
                id = EventSpotId(id.toString()),
                location = location,
                isReserved = isReserved,
                isPublished = isPublished,
            )
        }
    }
}
