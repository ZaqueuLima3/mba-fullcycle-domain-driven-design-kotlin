package dev.zaqueu.domaindrivendesignkotlin.api.eventspot

import dev.zaqueu.domaindrivendesignkotlin.api.event.models.EventResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models.SpotResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models.UpdateSpotRequest
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.event.services.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class EventSpotController(
    private val eventService: EventService
) : EventSpotApi {

    override fun updateById(
        eventId: String,
        sectionId: String,
        spotId: String,
        request: UpdateSpotRequest
    ): ResponseEntity<EventResponse> {
        val event = eventService.updateEventSpotLocation(request.toInput(eventId, sectionId, spotId))

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(EventResponse.fromDomain(event))
    }

    override fun listSpots(eventId: String, sectionId: String): ResponseEntity<List<SpotResponse>> {
        val events = eventService.findSpots(eventId, sectionId)
        val eventResponse = events.map { SpotResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(eventResponse)
    }
}
