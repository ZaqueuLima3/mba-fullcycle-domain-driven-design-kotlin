package dev.zaqueu.domaindrivendesignkotlin.api.customer

import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CreateCustomerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CustomerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CustomerResponse
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.UpdateCustomerRequest
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.customer.services.CustomerService
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
internal class CustomerController(
    private val customerService: CustomerService
) : CustomerApi {

    override fun createCustomer(request: CreateCustomerRequest): ResponseEntity<CustomerResponse> {
        val customer = customerService.register(request.toInput())
        return ResponseEntity
            .created(URI.create("/customers/${customer.id.value}"))
            .body(CustomerResponse.fromDomain(customer))
    }

    override fun updateById(id: String, request: UpdateCustomerRequest): ResponseEntity<CustomerResponse> {
        val customer = customerService.update(request.toInput(id))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CustomerResponse.fromDomain(customer))
    }

    override fun listCustomers(): ResponseEntity<List<CustomerListResponse>> {
        val customers = customerService.list()
        val customerResponse = customers.map { CustomerListResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(customerResponse)
    }

    override fun getById(id: String): ResponseEntity<CustomerResponse?> {
        val customer = customerService.findById(id) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok()
            .body(CustomerResponse.fromDomain(customer))
    }

    override fun deleteById(id: String) {
        customerService.delete(id)
    }
}
