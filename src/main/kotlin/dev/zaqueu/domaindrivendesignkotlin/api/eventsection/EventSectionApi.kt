package dev.zaqueu.domaindrivendesignkotlin.api.eventsection

import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.CreateSectionRequest
import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.EventSectionResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.SectionListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.eventsection.models.UpdateSectionRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/events/{eventId}/sections")
@Tag(name = "Event Sections")
internal interface EventSectionApi {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new section")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun createSection(
        @PathVariable(name = "eventId") eventId: String,
        @RequestBody request: CreateSectionRequest
    ): ResponseEntity<EventSectionResponse>

    @PutMapping(
        value = ["{sectionId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "Update a section by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Section updated successfully"),
            ApiResponse(responseCode = "404", description = "Section was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun updateById(
        @PathVariable(name = "eventId") eventId: String,
        @PathVariable(name = "sectionId") sectionId: String,
        @RequestBody request: UpdateSectionRequest
    ): ResponseEntity<EventSectionResponse>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "List all sections")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listSections(@PathVariable(name = "eventId") eventId: String): ResponseEntity<List<SectionListResponse>>
}
