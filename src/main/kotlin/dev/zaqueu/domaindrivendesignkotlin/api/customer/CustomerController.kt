package dev.zaqueu.domaindrivendesignkotlin.api.customer

import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CreateCustomerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CustomerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.UpdateCustomerRequest
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.services.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
internal class CustomerController(
    private val customerService: CustomerService
) : CustomerApi {

    override fun createCustomer(request: CreateCustomerRequest): ResponseEntity<String> {
        val customer = customerService.register(request.toInput())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("New customer ${customer.name} saved!")
    }

    override fun updateById(id: String, request: UpdateCustomerRequest): ResponseEntity<String> {
        val customer = customerService.update(request.toInput(id))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("Customer ${customer.name} updated!")
    }

    override fun listHouses(): ResponseEntity<List<CustomerListResponse>> {
        val customers = customerService.list()
        val customerResponse = customers.map { CustomerListResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(customerResponse)
    }
}
