package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.valueobject.OrderId

/**
 * id, customer_id, amount, event_spot_id, status.
 * status: PENDING, PAID, CANCELLED
 * amount = preço pago
 *
 * pay()
 *
 * cancel()
 */

/**
 * SpotReservation: spot_id (PK), reservation_date, customer_id
 */

internal data class Order(
    override val id: OrderId,
    val amount: Long,
    var status: Status,
    val customerId: CustomerId,
    val eventSpotId: EventSpotId,
) : AggregateRoot() {

    constructor(
        id: String? = null,
        amount: Long,
        status: Status,
        customerId: String,
        eventSpotId: String,
    ) : this(
        id = id.toDomainUuid(),
        amount = amount,
        status = status,
        customerId = customerId.toDomainUuid(),
        eventSpotId = eventSpotId.toDomainUuid(),
    )

    fun pay() {
        this.status = Status.PAID
    }

    fun cancel() {
        this.status = Status.CANCELLED
    }

    enum class Status {
        PENDING, PAID, CANCELLED
    }

    companion object {
        fun create(
            amount: Long,
            status: Status,
            customerId: String,
            eventSpotId: String,
        ): Order {
            return Order(
                amount = amount,
                status = status,
                customerId = customerId,
                eventSpotId = eventSpotId,
            )
        }
    }
}
