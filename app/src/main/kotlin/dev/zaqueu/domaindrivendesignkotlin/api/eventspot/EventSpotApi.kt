package dev.zaqueu.domaindrivendesignkotlin.api.eventspot

import dev.zaqueu.domaindrivendesignkotlin.api.event.models.EventResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models.SpotResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventspot.models.UpdateSpotRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/events/{eventId}/sections/{sectionId}/spots")
@Tag(name = "Event Spots")
internal interface EventSpotApi {
    @PutMapping(
        value = ["{spotId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "Update a spot location by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Spot updated successfully"),
            ApiResponse(responseCode = "404", description = "Spot was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun updateById(
        @PathVariable(name = "eventId") eventId: String,
        @PathVariable(name = "sectionId") sectionId: String,
        @PathVariable(name = "spotId") spotId: String,
        @RequestBody request: UpdateSpotRequest
    ): ResponseEntity<EventResponse>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "List all spots")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listSpots(
        @PathVariable(name = "eventId") eventId: String,
        @PathVariable(name = "sectionId") sectionId: String
    ): ResponseEntity<List<SpotResponse>>
}
