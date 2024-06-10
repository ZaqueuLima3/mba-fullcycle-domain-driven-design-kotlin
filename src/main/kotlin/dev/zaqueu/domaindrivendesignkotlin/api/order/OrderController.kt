package dev.zaqueu.domaindrivendesignkotlin.api.order

import dev.zaqueu.domaindrivendesignkotlin.api.order.models.CreateOrderRequest
import dev.zaqueu.domaindrivendesignkotlin.api.order.models.CreateOrderRequest.Companion.toInput
import dev.zaqueu.domaindrivendesignkotlin.api.order.models.OrderResponse
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.order.services.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
internal class OrderController(
    private val orderService: OrderService
) : OrderApi {

    override fun createOrder(eventId: String, request: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val order = orderService.create(request.toInput(eventId))
        return ResponseEntity
            .created(URI.create("/orders/${order.id.value}"))
            .body(OrderResponse.fromDomain(order))
    }

    override fun listOrders(eventId: String): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.list()
        val orderResponse = orders.map { OrderResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(orderResponse)
    }
}
