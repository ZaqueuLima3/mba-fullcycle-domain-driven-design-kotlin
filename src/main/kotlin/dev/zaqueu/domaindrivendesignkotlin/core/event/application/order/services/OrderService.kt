package dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.dto.CreateOrderDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.valueobject.CustomerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.SpotReservation
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.OrderRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.SpotReservationRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
internal class OrderService(
    private val orderRepository: OrderRepository,
    private val eventRepository: EventRepository,
    private val customerRepository: CustomerRepository,
    private val spotReservationRepository: SpotReservationRepository,
    private val unitOfWork: UnitOfWork
) {
    fun list(): List<Order> {
        return orderRepository.findAll()
    }

    fun create(input: CreateOrderDto): Order {
        spotReservationRepository.findById(input.spotId.toDomainUuid<EventSpotId>())?.let {
            throw Exception("Spot already reserved")
        }

        customerRepository.findById(input.customerId.toDomainUuid<CustomerId>())
            ?: throw Exception("Customer not found")

        val event = eventRepository.findById(input.eventId.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        val section = event.getSection(input.sectionId.toDomainUuid())
            ?: throw Exception("Section not found")

        val spot = section.getSpot(input.spotId.toDomainUuid())
            ?: throw Exception("Spot not found")

        if (!event.allowReserveSpot(section.id, spot.id)) throw Exception("Spot not available")

        event.reserveSpot(
            sectionId = input.sectionId.toDomainUuid(),
            spotId = input.spotId.toDomainUuid(),
        )

        val spotReservation = SpotReservation.create(
            id = input.spotId,
            reservationDate = Instant.now(),
            customerId = input.customerId,
        )

        val order = Order.create(
            amount = section.price,
            status = Order.Status.PENDING,
            customerId = input.customerId,
            eventSpotId = input.spotId,
        )

        spotReservationRepository.add(spotReservation)
        eventRepository.add(event)
        orderRepository.add(order)
        unitOfWork.commit()

        return order
    }
}
