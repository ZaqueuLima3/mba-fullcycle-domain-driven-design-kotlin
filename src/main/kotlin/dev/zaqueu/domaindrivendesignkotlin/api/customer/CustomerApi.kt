package dev.zaqueu.domaindrivendesignkotlin.api.customer

import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CreateCustomerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CustomerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.UpdateCustomerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/customers")
@Tag(name = "Customers")
internal interface CustomerApi {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new customer")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun createCustomer(@RequestBody request: CreateCustomerRequest): ResponseEntity<String>

    @PutMapping(
        value = ["{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Operation(summary = "Update a customer by it's identifier")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            ApiResponse(responseCode = "404", description = "Customer was not found"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun updateById(
        @PathVariable(name = "id") id: String,
        @RequestBody request: UpdateCustomerRequest
    ): ResponseEntity<String>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "List all customers")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listHouses(): ResponseEntity<List<CustomerListResponse>>;
}
