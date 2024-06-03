package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import jakarta.persistence.*
import java.util.*

@Entity(name = "orders")
internal data class OrderEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "amount")
    var amount: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Order.Status,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerEntity,

    @ManyToOne
    @JoinColumn(name = "event_spot_id")
    var eventSpot: EventSpotEntity
) {
    companion object {
        fun fromDomain(order: Order, customer: CustomerEntity, eventSpot: EventSpotEntity): OrderEntity {
            return OrderEntity(
                id = order.id.toUUID(),
                amount = order.amount,
                status = order.status,
                customer = customer,
                eventSpot = eventSpot
            )
        }

        fun OrderEntity.toDomain(): Order {
            return Order(
                id = id.toDomainUuid(),
                amount = amount,
                status = status,
                customerId = customer.id.toDomainUuid(),
                eventSpotId = eventSpot.id.toDomainUuid()
            )
        }
    }
}
