package dev.zaqueu.domaindrivendesignkotlin.api.eventsection

import dev.zaqueu.domaindrivendesignkotlin.api.event.models.EventResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.CreateSectionRequest
import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.SectionResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.UpdateSectionRequest
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
internal class EventSectionController(
    private val eventService: EventService
) : EventSectionApi {

    override fun createSection(eventId: String, request: CreateSectionRequest): ResponseEntity<EventResponse> {
        val event = eventService.addSection(request.toInput(eventId))
        return ResponseEntity
            .created(URI.create("/events/${event.id.value}"))
            .body(EventResponse.fromDomain(event))
    }

    override fun updateById(
        eventId: String,
        sectionId: String,
        request: UpdateSectionRequest
    ): ResponseEntity<EventResponse> {
        val event = eventService.updateEventSection(request.toInput(sectionId, eventId))

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(EventResponse.fromDomain(event))
    }

    override fun listSections(eventId: String): ResponseEntity<List<SectionResponse>> {
        val events = eventService.findSections(eventId)
        val eventResponse = events.map { SectionResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventResponse)
    }
}
