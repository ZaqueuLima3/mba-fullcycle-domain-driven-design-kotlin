package dev.zaqueu.domaindrivendesignkotlin.api.partner

import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.CreatePartnerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.PartnerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.PartnerResponse
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.UpdatePartnerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/partners")
@Tag(name = "Partners")
internal interface PartnerApi {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new partner")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun createPartner(@RequestBody request: CreatePartnerRequest): ResponseEntity<PartnerResponse>

    @PutMapping(
        value = ["{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "Update a partner by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Partner updated successfully"),
            ApiResponse(responseCode = "404", description = "Partner was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun updateById(
        @PathVariable(name = "id") id: String,
        @RequestBody request: UpdatePartnerRequest
    ): ResponseEntity<PartnerResponse>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "List all partners")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listPartners(): ResponseEntity<List<PartnerListResponse>>

    @GetMapping(
        value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Get a partner by id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Get successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request body"),
            ApiResponse(responseCode = "404", description = "Partner was not found"),
            ApiResponse(responseCode = "500", description = "Internal server error"),
        ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable(name = "id") id: String): ResponseEntity<PartnerResponse?>

    @DeleteMapping(
        value = ["{id}"],
    )
    @Operation(summary = "Delete a partner by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Partner deleted successfully"),
            ApiResponse(responseCode = "404", description = "Partner was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable(name = "id") id: String)
}
