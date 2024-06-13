package dev.zaqueu.domaindrivendesignkotlin.api.order

import dev.zaqueu.domaindrivendesignkotlin.api.order.models.CreateOrderRequest
import dev.zaqueu.domaindrivendesignkotlin.api.order.models.OrderResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/events/{eventId}/orders")
@Tag(name = "Orders")
internal interface OrderApi {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new Order")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created successfully"),
            ApiResponse(responseCode = "403", description = "Event or spot not allowed to reserve"),
            ApiResponse(responseCode = "409", description = "Trying to reserve a spot that is already reserved"),
            ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
        ]
    )
    fun createOrder(
        @PathVariable(name = "eventId") eventId: String,
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<OrderResponse>

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "List all Orders")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listed successfully"),
            ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            ApiResponse(responseCode = "500", description = "An internal server error was thrown")
        ]
    )
    fun listOrders(@PathVariable(name = "eventId") eventId: String): ResponseEntity<List<OrderResponse>>
}
