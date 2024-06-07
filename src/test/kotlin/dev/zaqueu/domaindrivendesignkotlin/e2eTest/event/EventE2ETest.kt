package dev.zaqueu.domaindrivendesignkotlin.e2eTest.event

import dev.zaqueu.domaindrivendesignkotlin.E2ETest
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.CreateEventRequest
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.EventResponse
import dev.zaqueu.domaindrivendesignkotlin.api.event.models.UpdateEventRequest
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.CreatePartnerRequest
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
class EventE2ETest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `should be able to create a new event with valid values and retrieve it`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val expectedName = "Lollapalooza 2021"
        val expectedDescription = "Music festival"
        val expectedDate = "2021-11-25T15:20:00Z"

        val partnerId = givenAPartner("Lollapalooza")

        val eventId = givenAEvent(
            name = expectedName,
            description = expectedDescription,
            date = expectedDate,
            partnerId = partnerId.orEmpty(),
        )

        Assertions.assertNotNull(eventId)

        val event = retrieveAEvent(eventId!!)

        Assertions.assertEquals(eventId, event.id)
        Assertions.assertNotNull(expectedName, event.name)
        Assertions.assertNotNull(expectedDescription, event.description)
        Assertions.assertNotNull(expectedDate, event.date)
        Assertions.assertNotNull(partnerId, event.partnerId)
    }

    @Test
    fun `should be able to list the event`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val partnerId = givenAPartner("Lollapalooza")

        val eventId1 = givenAEvent(
            name = "Lollapalooza 2021",
            description = "Music festival",
            date = "2021-11-25T15:20:00Z",
            partnerId = partnerId.orEmpty(),
        )

        val eventId2 = givenAEvent(
            name = "Joao Rock",
            description = "Music festival",
            date = "2021-11-25T15:20:00Z",
            partnerId = partnerId.orEmpty(),
        )

        val request = MockMvcRequestBuilders.get("/events")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val events = json.decodeFromString<List<EventListResponse>>(response)

        Assertions.assertEquals(2, events.size)
        Assertions.assertTrue(events.firstOrNull { it.id == eventId1 } != null)
        Assertions.assertTrue(events.firstOrNull { it.id == eventId2 } != null)
    }

    @Test
    fun `should be able to update a event`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val input = UpdateEventRequest(
            name = "Lollapalooza 2022"
        )

        val partnerId = givenAPartner("Lollapalooza")

        val eventId = givenAEvent(
            name = "Lollapalooza 2021",
            description = "Music festival",
            date = "2021-11-25T15:20:00Z",
            partnerId = partnerId.orEmpty(),
        )

        val request = MockMvcRequestBuilders.put("/events/$eventId")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request).andExpect(status().isOk)

        val event = retrieveAEvent(eventId!!)

        Assertions.assertEquals(eventId, event.id)
        Assertions.assertEquals(input.name, event.name)
    }

    @Test
    fun `should be able to delete a event`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val input = UpdateEventRequest(
            name = "Lollapalooza 2022"
        )

        val partnerId = givenAPartner("Lollapalooza")

        val eventId = givenAEvent(
            name = "Lollapalooza 2021",
            description = "Music festival",
            date = "2021-11-25T15:20:00Z",
            partnerId = partnerId.orEmpty(),
        )

        val request = MockMvcRequestBuilders.delete("/events/$eventId")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request).andExpect(status().isNoContent)
    }

    private fun givenAEvent(name: String, description: String, date: String, partnerId: String): String? {
        val input = CreateEventRequest(name, description, date, partnerId)

        val request = MockMvcRequestBuilders.post("/events")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val eventId = mvc.perform(request)
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .getHeader("Location")


        return eventId?.replace("/events/", "")
    }

    private fun retrieveAEvent(id: String): EventResponse {
        val request = MockMvcRequestBuilders.get("/events/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        return json.decodeFromString<EventResponse>(response)
    }

    private fun givenAPartner(name: String): String? {
        val input = CreatePartnerRequest(name)

        val request = MockMvcRequestBuilders.post("/partners")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val partnerId = mvc.perform(request)
            .andExpect(status().isCreated)
            .andReturn()
            .response
            .getHeader("Location")


        return partnerId?.replace("/partners/", "")
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
