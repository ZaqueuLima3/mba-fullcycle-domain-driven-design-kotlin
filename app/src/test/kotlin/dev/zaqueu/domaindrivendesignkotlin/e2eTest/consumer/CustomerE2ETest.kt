package dev.zaqueu.domaindrivendesignkotlin.e2eTest.consumer

import dev.zaqueu.domaindrivendesignkotlin.E2ETest
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CreateCustomerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CustomerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.CustomerResponse
import dev.zaqueu.domaindrivendesignkotlin.api.customer.models.UpdateCustomerRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
@Testcontainers
class CustomerE2ETest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `should be able to create a new customer with valid values and retrieve it`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val expectedName = "John Doe"
        val expectedCpf = "93928642057"

        val customerId = givenACustomer(expectedName, expectedCpf)

        Assertions.assertNotNull(customerId)

        val customer = retrieveACustomer(customerId!!)

        Assertions.assertEquals(customerId, customer.id)
        Assertions.assertNotNull(expectedName, customer.name)
        Assertions.assertNotNull(expectedCpf, customer.cpf)
    }

    @Test
    fun `should be able to list the customer`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        givenACustomer("John Doe", "93928642057")
        givenACustomer("Mark Ki", "83736117035")

        val request = MockMvcRequestBuilders.get("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val customers = json.decodeFromString<List<CustomerListResponse>>(response)

        Assertions.assertEquals(2, customers.size)
        Assertions.assertTrue(customers.firstOrNull { it.cpf == "83736117035" } != null)
        Assertions.assertTrue(customers.firstOrNull { it.cpf == "93928642057" } != null)
    }

    @Test
    fun `should be able to update a customer`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val input = UpdateCustomerRequest(
            name = "Mark Ki"
        )

        val customerId = givenACustomer("John Doe", "93928642057")

        val request = MockMvcRequestBuilders.put("/customers/$customerId")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request).andExpect(status().isOk)

        val customer = retrieveACustomer(customerId!!)

        Assertions.assertEquals(customerId, customer.id)
        Assertions.assertEquals(input.name, customer.name)
    }

    @Test
    fun `should be able to delete a customer`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val input = UpdateCustomerRequest(
            name = "Mark Ki"
        )

        val customerId = givenACustomer("John Doe", "93928642057")

        val request = MockMvcRequestBuilders.delete("/customers/$customerId")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request).andExpect(status().isNoContent)
    }

    private fun givenACustomer(name: String, cpf: String): String? {
        val input = CreateCustomerRequest(name, cpf)

        val request = MockMvcRequestBuilders.post("/customers")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val customerId = mvc.perform(request)
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .getHeader("Location")


        return customerId?.replace("/customers/", "")
    }

    private fun retrieveACustomer(id: String): CustomerResponse {
        val request = MockMvcRequestBuilders.get("/customers/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        return json.decodeFromString<CustomerResponse>(response)
    }

    companion object {
        @Container
        private val MYSQL_CONTAINER: MySQLContainer<*> = MySQLContainer("mysql:latest")
            .withPassword("root")
            .withUsername("root")
            .withDatabaseName("e2e_events")

        @JvmStatic
        @DynamicPropertySource
        fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl)
            registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername)
            registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword)
        }
    }
}
