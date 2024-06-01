package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event.EventSectionId
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSpotEntity.Companion.toDomain
import jakarta.persistence.*
import java.util.*

@Entity(name = "event_sections")
internal class EventSectionEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "name")
    var name: String,

    @Column(name = "description")
    var description: String?,

    @Column(name = "is_published")
    var isPublished: Boolean,

    @Column(name = "total_spots")
    var totalSpots: Long,

    @Column(name = "total_spots_reserved")
    var totalSpotsReserved: Long,

    @Column(name = "price")
    var price: Long,

    @ManyToOne
    @JoinColumn(name = "event_id")
    var event: EventEntity
) {
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "event_section_id")
    var spots: MutableSet<EventSpotEntity> = mutableSetOf()

    companion object {
        fun fromDomain(eventSection: EventSection, event: EventEntity): EventSectionEntity {
            val sectionEventEntity = EventSectionEntity(
                id = UUID.fromString(eventSection.id.value),
                name = eventSection.name,
                description = eventSection.description,
                isPublished = eventSection.isPublished,
                totalSpots = eventSection.totalSpots,
                totalSpotsReserved = eventSection.totalSpotsReserved.toLong(),
                price = eventSection.price,
                event = event,
            )

            sectionEventEntity.spots = eventSection.spots.map {
                EventSpotEntity.fromDomain(it, sectionEventEntity)
            }.toMutableSet()

            return sectionEventEntity
        }

        fun EventSectionEntity.toDomain(): EventSection {
            val section = EventSection(
                id = EventSectionId(id.toString()),
                name = name,
                description = description,
                isPublished = isPublished,
                totalSpots = totalSpots,
                price = price,
            )

            section.addSpots(spots.map { it.toDomain() }.toSet())

            return section
        }
    }
}
