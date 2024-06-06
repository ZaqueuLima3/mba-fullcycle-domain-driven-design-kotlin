package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSectionEntity.Companion.toDomain
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "events")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    var partner: PartnerEntity,
) {
    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    var sections: MutableSet<EventSectionEntity> = mutableSetOf()

    companion object {
        fun fromDomain(event: Event, partner: PartnerEntity): EventEntity {
            val eventEntity = EventEntity(
                id = event.id.toUUID(),
                name = event.name,
                description = event.description,
                date = event.date,
                isPublished = event.isPublished,
                totalSpots = event.totalSpots,
                totalSpotsReserved = event.totalSpotsReserved,
                partner = partner,
            )

            eventEntity.sections = event.sections.map { EventSectionEntity.fromDomain(it, eventEntity) }.toMutableSet()

            return eventEntity
        }

        fun EventEntity.toDomain(): Event {
            val event = Event(
                id = id.toString(),
                name = name,
                description = description,
                date = date,
                isPublished = isPublished,
                totalSpotsReserved = totalSpotsReserved,
                partnerId = partner.id.toString(),
            )

            event.addSections(sections.map { it.toDomain() }.toSet())

            return event
        }
    }
}
