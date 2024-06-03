package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
import java.time.Instant

internal data class SpotReservation(
    override val id: EventSpotId,
    val reservationDate: Instant,
    val customerId: CustomerId,
) : AggregateRoot() {

    companion object {
        fun create(
            id: String,
            reservationDate: Instant,
            customerId: String,
        ): SpotReservation {
            return SpotReservation(
                id = id.toDomainUuid(),
                reservationDate = reservationDate,
                customerId = customerId.toDomainUuid(),
            )
        }
    }
}
