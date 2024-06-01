package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSectionEntity.Companion.toDomain
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity(name = "events")
internal class EventEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "name")
    var name: String,

    @Column(name = "description")
    var description: String?,

    @Column(name = "date")
    var date: Instant,

    @Column(name = "is_published")
    var isPublished: Boolean,

    @Column(name = "total_spots")
    var totalSpots: Long,

    @Column(name = "total_spots_reserved")
    var totalSpotsReserved: Long,

    @Column(name = "partner_id")
    var partnerId: UUID,
) {
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "event_id")
    var sections: MutableSet<EventSectionEntity> = mutableSetOf()

    companion object {
        fun fromDomain(event: Event): EventEntity {
            val eventEntity = EventEntity(
                id = UUID.fromString(event.id.value),
                name = event.name,
                description = event.description,
                date = event.date,
                isPublished = event.isPublished,
                totalSpots = event.totalSpots,
                totalSpotsReserved = event.totalSpotsReserved,
                partnerId = UUID.fromString(event.partnerId.value),
            )

            eventEntity.sections = event.sections.map { EventSectionEntity.fromDomain(it, eventEntity) }.toMutableSet()

            return eventEntity
        }

        fun EventEntity.toDomain(): Event {
            return Event(
                id = id.toString(),
                name = name,
                description = description,
                date = date,
                isPublished = isPublished,
                totalSpotsReserved = totalSpotsReserved,
                partnerId = partnerId.toString(),
                sections = sections.map { it.toDomain() }.toSet()
            )
        }
    }
}