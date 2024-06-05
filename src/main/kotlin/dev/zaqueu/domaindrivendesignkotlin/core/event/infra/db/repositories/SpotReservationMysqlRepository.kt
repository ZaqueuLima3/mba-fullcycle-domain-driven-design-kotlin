package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.SpotReservation
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.SpotReservationRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.CustomerEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSpotEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.SpotReservationEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.SpotReservationEntity.Companion.toDomain
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class SpotReservationMysqlRepository(
    private val entityManager: EntityManager
) : SpotReservationRepository {

    private fun getEntities(customerId: UUID, spotId: UUID): Pair<CustomerEntity, EventSpotEntity> {
        val customer = entityManager.find(CustomerEntity::class.java, customerId)
            ?: throw Exception("Customer not found")

        val eventSpot = entityManager.find(EventSpotEntity::class.java, spotId)
            ?: throw Exception("Spot not found")

        return customer to eventSpot
    }

    override fun add(entity: SpotReservation) {
        val (customer, eventSpot) = getEntities(entity.customerId.toUUID(), entity.id.toUUID())
        entityManager.persist(SpotReservationEntity.fromDomain(entity, customer, eventSpot))
    }

    override fun update(entity: SpotReservation) {
        throw UnsupportedOperationException("Update operation is not supported by this repository")
    }

    override fun findById(id: Uuid): SpotReservation? {
        val entity = entityManager.find(SpotReservationEntity::class.java, id.toUUID())
        return entity?.toDomain()
    }

    override fun findAll(): List<SpotReservation> {
        val query = entityManager.createQuery("SELECT c FROM spot_reservations c", SpotReservationEntity::class.java)
        val entities = query.resultList
        return entities.map { it.toDomain() }
    }

    override fun delete(id: Uuid) {
        val partnerEntity = entityManager.find(SpotReservationEntity::class.java, id.toUUID())
        partnerEntity?.let { entityManager.remove(it) }
    }

    override fun deleteAll() {
        entityManager.createQuery("DELETE FROM customers").executeUpdate()
    }
}
