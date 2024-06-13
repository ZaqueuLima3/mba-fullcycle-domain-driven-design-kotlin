package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.ResourceNotFoundException
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.valueobject.EventSpotId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.SpotReservation
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.SpotReservationRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.SpotReservationEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@IntegrationTest
class SpotReservationMysqlRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var spotReservationRepository: SpotReservationRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    private lateinit var customer: Customer

    private lateinit var event: Event

    private lateinit var partner: Partner

    private lateinit var spotId: EventSpotId

    @BeforeEach
    fun setup() {
        customer = Customer.create(
            name = "Test SpotReservation",
            cpf = "93928642057",
        )

        partner = Partner.create(
            name = "Test Name"
        )

        event = Event.create(
            name = "event",
            description = "description",
            date = Instant.now(),
            partnerId = partner.id.value
        )

        event.addSection(
            name = "Section",
            description = "description",
            totalSpots = 1,
            price = 1000,
        )

        spotId = event.sections.first().spots.first().id

        customerRepository.add(customer)
        partnerRepository.add(partner)
        eventRepository.add(event)

        entityManager.flush()
        entityManager.clear()
    }

    @Test
    @Transactional
    fun `should add a new spot reservation`() {
        val spotReservation = SpotReservation.create(
            id = spotId.value,
            customerId = customer.id.value,
            reservationDate = Instant.now()
        )

        spotReservationRepository.add(spotReservation)
        entityManager.flush()
        entityManager.clear()

        val spotReservationEntity = entityManager.find(SpotReservationEntity::class.java, spotReservation.id.toUUID())
        Assertions.assertNotNull(spotReservationEntity)
        Assertions.assertEquals(spotReservation.id.value, spotReservationEntity.id.toString())
    }

    @Test
    @Transactional
    fun `should throw an exception when try to update a spot reservation`() {
        val expectedErrorMessage = "Update operation is not supported by this repository"
        val spotReservation = SpotReservation.create(
            id = spotId.value,
            customerId = customer.id.value,
            reservationDate = Instant.now()
        )

        val actualException = Assertions.assertThrows(UnsupportedOperationException::class.java) {
            spotReservationRepository.update(spotReservation)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    @Transactional
    fun `should throws an Exception when try to add spotReservation with a non existent customer`() {
        val expectedCustomerId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Customer with id $expectedCustomerId not found"

        val spotReservation = SpotReservation.create(
            id = spotId.value,
            customerId = expectedCustomerId,
            reservationDate = Instant.now()
        )

        val actualException = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            spotReservationRepository.add(spotReservation)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    @Transactional
    fun `should throws an Exception when try to add spotReservation with a non existent spot`() {
        val expectedSpotId = UUID.randomUUID().toString()
        val expectedErrorMessage = "Spot with id $expectedSpotId not found"
        val spotReservation = SpotReservation.create(
            id = expectedSpotId,
            customerId = customer.id.value,
            reservationDate = Instant.now()
        )

        val actualException = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            spotReservationRepository.add(spotReservation)
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }


    @Test
    @Transactional
    fun `should return an spotReservation when it is found`() {
        val spotReservation = SpotReservation.create(
            id = spotId.value,
            customerId = customer.id.value,
            reservationDate = Instant.now()
        )

        spotReservationRepository.add(spotReservation)
        entityManager.flush()
        entityManager.clear()

        val savedSpotReservation = spotReservationRepository.findById(spotReservation.id)
        Assertions.assertNotNull(savedSpotReservation)
        Assertions.assertEquals(spotReservation.id, savedSpotReservation?.id)
    }

    @Test
    @Transactional
    fun `should return null when spotReservation is no found`() {
        val savedOrder = spotReservationRepository.findById(UUID.randomUUID().toDomainUuid<EventSpotId>())
        Assertions.assertNull(savedOrder)
    }

    @Test
    @Transactional
    fun `should return a list of spotReservations`() {
        val spotReservation = SpotReservation.create(
            id = spotId.value,
            customerId = customer.id.value,
            reservationDate = Instant.now()
        )

        spotReservationRepository.add(spotReservation)
        entityManager.flush()
        entityManager.clear()

        val spotReservations = spotReservationRepository.findAll()

        Assertions.assertTrue(spotReservations.size == 1)
        Assertions.assertTrue(spotReservations.contains(spotReservation))
    }

    @Test
    @Transactional
    fun `should return an empty list of spotReservations`() {
        val spotReservations = spotReservationRepository.findAll()
        Assertions.assertTrue(spotReservations.isEmpty())
    }

    @Test
    @Transactional
    fun `should delete an spotReservation when it is found`() {
        val spotReservation = SpotReservation.create(
            id = spotId.value,
            customerId = customer.id.value,
            reservationDate = Instant.now()
        )

        spotReservationRepository.add(spotReservation)
        entityManager.flush()
        entityManager.clear()

        val savedSpotReservation = entityManager.find(SpotReservationEntity::class.java, spotReservation.id.toUUID())
        Assertions.assertNotNull(savedSpotReservation)
        Assertions.assertEquals(spotReservation.id.value, savedSpotReservation?.id.toString())

        spotReservationRepository.delete(spotReservation.id)

        val deletedSpotReservation = entityManager.find(SpotReservationEntity::class.java, spotReservation.id.toUUID())

        Assertions.assertNull(deletedSpotReservation)
    }

    @Test
    @Transactional
    fun `should do nothing when doesn't find an spotReservation to delete`() {
        Assertions.assertDoesNotThrow {
            spotReservationRepository.delete(UUID.randomUUID().toDomainUuid<EventSpotId>())
        }
    }
}
