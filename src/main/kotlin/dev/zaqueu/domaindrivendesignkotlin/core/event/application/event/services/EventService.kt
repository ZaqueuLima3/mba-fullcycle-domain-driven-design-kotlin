package dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.CreateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.dto.UpdateEventDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.EventSection
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.toEventId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.toPartnerId
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

    fun findSections(id: String): List<EventSection> {
        val event = eventRepository.findById(id.toEventId()) ?: throw Exception("Event not found")

        return event.sections.toList()
    }

    fun create(input: CreateEventDto): Event {
        val partner = partnerRepository.findById(input.partnerId.toPartnerId()) ?: throw Exception("Partner not found")

        val event = partner.initializeEvent(
            name = input.name,
            description = input.description.orEmpty(),
            date = input.date,
        )

        eventRepository.add(event)
        unitOfWork.commit()
        return event
    }

    fun update(input: UpdateEventDto): Event {
        val event = eventRepository.findById(input.id.toEventId()) ?: throw Exception("Event not found")

        if (!input.name.isNullOrBlank()) event.changeName(input.name)
        if (input.description != null) event.changeDescription(input.description)
        if (input.date != null) event.changeDate(input.date)

        when (input.isPublished) {
            true -> event.publish()
            false -> event.unPublish()
            null -> Unit
        }

        eventRepository.add(event)
        unitOfWork.commit()

        return event
    }
}
