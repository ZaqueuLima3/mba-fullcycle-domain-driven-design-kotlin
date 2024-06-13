package dev.zaqueu.domaindrivendesignkotlin.e2eTest.partner

import dev.zaqueu.domaindrivendesignkotlin.E2ETest
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.CreatePartnerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.PartnerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.PartnerResponse
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.UpdatePartnerRequest
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
class PartnerE2ETest {

    @Autowired
    private lateinit var mvc: MockMvc

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Test
    fun `should be able to create a new partner with valid values and retrieve it`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val expectedName = "Lollapalooza"

        val partnerId = givenAPartner(expectedName)

        Assertions.assertNotNull(partnerId)

        val partner = retrieveAPartner(partnerId!!)

        Assertions.assertEquals(partnerId, partner.id)
        Assertions.assertNotNull(expectedName, partner.name)
    }

    @Test
    fun `should be able to list the partner`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val partnerId1 = givenAPartner("Lollapalooza")
        val partnerId2 = givenAPartner("João Rock")

        val request = MockMvcRequestBuilders.get("/partners")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val partners = json.decodeFromString<List<PartnerListResponse>>(response)

        Assertions.assertEquals(2, partners.size)
        Assertions.assertTrue(partners.firstOrNull { it.id == partnerId1 } != null)
        Assertions.assertTrue(partners.firstOrNull { it.id == partnerId2 } != null)
    }

    @Test
    fun `should be able to update a partner`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val input = UpdatePartnerRequest(
            name = "Joao Rock"
        )

        val partnerId = givenAPartner("Lollapalooza")

        val request = MockMvcRequestBuilders.put("/partners/$partnerId")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request).andExpect(status().isOk)

        val partner = retrieveAPartner(partnerId!!)

        Assertions.assertEquals(partnerId, partner.id)
        Assertions.assertEquals(input.name, partner.name)
    }

    @Test
    fun `should be able to delete a partner`() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning())

        val input = UpdatePartnerRequest(
            name = "João Rock"
        )

        val partnerId = givenAPartner("Lollapalooza")

        val request = MockMvcRequestBuilders.delete("/partners/$partnerId")
            .content(json.encodeToString(input))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request).andExpect(status().isNoContent)
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

    private fun retrieveAPartner(id: String): PartnerResponse {
        val request = MockMvcRequestBuilders.get("/partners/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        return json.decodeFromString<PartnerResponse>(response)
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
