package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.SpotReservation
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity(name = "spot_reservations")
internal data class SpotReservationEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "reservation_date")
    var reservationDate: Instant,

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    var eventSpot: EventSpotEntity,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerEntity,
) {
    companion object {
        fun fromDomain(
            spotReservation: SpotReservation,
            customer: CustomerEntity,
            eventSpot: EventSpotEntity
        ): SpotReservationEntity {
            return SpotReservationEntity(
                id = spotReservation.id.toUUID(),
                reservationDate = spotReservation.reservationDate,
                eventSpot = eventSpot,
                customer = customer
            )
        }

        fun SpotReservationEntity.toDomain(): SpotReservation {
            return SpotReservation(
                id = id.toDomainUuid(),
                reservationDate = reservationDate,
                customerId = customer.id.toDomainUuid(),
            )
        }
    }
}
