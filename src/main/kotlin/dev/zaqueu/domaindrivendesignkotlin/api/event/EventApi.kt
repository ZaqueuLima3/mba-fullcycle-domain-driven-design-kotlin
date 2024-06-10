package dev.zaqueu.domaindrivendesignkotlin.api.event

import dev.zaqueu.domaindrivendesignkotlin.api.event.models.CreateEventRequest
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.EventResponse
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.UpdateEventRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/events")
@Tag(name = "Events")
internal interface EventApi {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new event")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun createEvent(@RequestBody request: CreateEventRequest): ResponseEntity<EventResponse>

    @PutMapping(
        value = ["{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "Update a event by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Event updated successfully"),
            ApiResponse(responseCode = "404", description = "Event was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun updateById(
        @PathVariable(name = "id") id: String,
        @RequestBody request: UpdateEventRequest
    ): ResponseEntity<EventResponse>

    @PutMapping(
        value = ["{id}/publish-all"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "Publish an event by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Event published successfully"),
            ApiResponse(responseCode = "404", description = "Event was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun publishAll(
        @PathVariable(name = "id") id: String
    ): ResponseEntity<EventResponse>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "List all events")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listEvents(): ResponseEntity<List<EventResponse>>

    @GetMapping(
        value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Get a event by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Get successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request body"),
            ApiResponse(responseCode = "404", description = "Event was not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable(name = "id") id: String): ResponseEntity<EventResponse?>

    @DeleteMapping(
        value = ["{id}"],
    )
    @Operation(summary = "Delete a event by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            ApiResponse(responseCode = "404", description = "Event was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable(name = "id") id: String)
}
