package dev.zaqueu.domaindrivendesignkotlin.api.event

import dev.zaqueu.domaindrivendesignkotlin.api.event.models.CreateEventRequest
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.EventResponse
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.UpdateEventRequest
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
internal class EventController(
    private val eventService: EventService
) : EventApi {

    override fun createEvent(request: CreateEventRequest): ResponseEntity<EventResponse> {
        val event = eventService.create(request.toInput())
        return ResponseEntity
            .created(URI.create("/events/${event.id.value}"))
            .body(EventResponse.fromDomain(event))
    }

    override fun updateById(id: String, request: UpdateEventRequest): ResponseEntity<EventResponse> {
        val event = eventService.update(request.toInput(id))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(EventResponse.fromDomain(event))
    }

    override fun listEvents(): ResponseEntity<List<EventResponse>> {
        val events = eventService.findEvents()
        val eventResponse = events.map { EventResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventResponse)
    }

    override fun getById(id: String): ResponseEntity<EventResponse?> {
        val event = eventService.findById(id) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok()
            .body(EventResponse.fromDomain(event))
    }

    override fun deleteById(id: String) {
        eventService.delete(id)
    }
}
