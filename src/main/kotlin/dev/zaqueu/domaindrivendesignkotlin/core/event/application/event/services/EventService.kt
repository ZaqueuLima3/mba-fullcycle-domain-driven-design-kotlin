package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.*
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.CreateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.CreateEventSectionDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventSectionDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSpot
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import org.springframework.stereotype.Service

@Service
internal class EventService(
    private val eventRepository: EventRepository,
    private val partnerRepository: PartnerRepository,
    private val unitOfWork: UnitOfWork
) {
    fun list(): List<Event> {
        return eventRepository.findAll()
    }

    fun findSections(eventId: String): List<EventSection> {
        val event = eventRepository.findById(eventId.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        return event.sections.toList()
    }

    fun findSpots(eventId: String, sectionId: String): List<EventSpot> {
        val event = eventRepository.findById(eventId.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")
        val section = event.sections.find { it.id.value == sectionId }
            ?: throw Exception("Section not found")

        return section.spots.toList()
    }

    fun create(input: CreateEventDto): Event {
        val partner = partnerRepository.findById(input.partnerId.toDomainUuid<PartnerId>())
            ?: throw Exception("Partner not found")

        val event = partner.initializeEvent(
            name = input.name,
            description = input.description.orEmpty(),
            date = input.date,
        )

        eventRepository.add(event)
        unitOfWork.commit()
        return event
    }

    fun addSection(input: CreateEventSectionDto): Event {
        val event = eventRepository.findById(input.eventId.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        event.addSection(
            name = input.name,
            description = input.description,
            totalSpots = input.totalSpots,
            price = input.price,
        )

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }

    fun update(input: UpdateEventDto): Event {
        val event = eventRepository.findById(input.id.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        if (!input.name.isNullOrBlank()) event.changeName(input.name)
        if (input.description != null) event.changeDescription(input.description)
        if (input.date != null) event.changeDate(input.date)

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }

    fun updateEventSection(input: UpdateEventSectionDto): Event {
        val event = eventRepository.findById(input.eventId.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        event.changeSectionInformation(
            sectionId = input.sectionId.toDomainUuid(),
            name = input.name,
            description = input.description,
            price = input.price,
        )

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }

    fun updateEventSpotLocation(input: UpdateEventSpotLocationDto): Event {
        val event = eventRepository.findById(input.eventId.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        event.changeSpotLocation(
            sectionId = input.sectionId.toDomainUuid(),
            spotId = input.spotId.toDomainUuid(),
            location = input.location,
        )

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }

    fun publishAll(id: String): Event {
        val event = eventRepository.findById(id.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        event.publishAll()

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }

    fun unPublishAll(id: String): Event {
        val event = eventRepository.findById(id.toDomainUuid<EventId>())
            ?: throw Exception("Event not found")

        event.unPublishAll()

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }
}
