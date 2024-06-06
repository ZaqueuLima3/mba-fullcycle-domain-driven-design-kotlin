package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories.OrderRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.CustomerEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSpotEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.OrderEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.OrderEntity.Companion.toDomain
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class OrderMysqlRepository(
    private val entityManager: EntityManager
) : OrderRepository {

    private fun getEntities(customerId: UUID, spotId: UUID): Pair<CustomerEntity, EventSpotEntity> {
        val customer = entityManager.find(CustomerEntity::class.java, customerId)
            ?: throw Exception("Customer not found")

        val eventSpot = entityManager.find(EventSpotEntity::class.java, spotId)
            ?: throw Exception("Spot not found")

        return customer to eventSpot
    }

    override fun add(entity: Order) {
        val (customer, eventSpot) = getEntities(entity.customerId.toUUID(), entity.eventSpotId.toUUID())
        entityManager.persist(OrderEntity.fromDomain(entity, customer, eventSpot))
    }

    override fun update(entity: Order) {
        val (customer, eventSpot) = getEntities(entity.customerId.toUUID(), entity.eventSpotId.toUUID())
        entityManager.merge(OrderEntity.fromDomain(entity, customer, eventSpot))
    }

    override fun findById(id: Uuid): Order? {
        val entity = entityManager.find(OrderEntity::class.java, id.toUUID())
        return entity?.toDomain()
    }

    override fun findAll(): List<Order> {
        val query = entityManager.createQuery("SELECT c FROM orders c", OrderEntity::class.java)
        val entities = query.resultList
        return entities.map { it.toDomain() }
    }

    override fun delete(id: Uuid) {
        val partnerEntity = entityManager.find(OrderEntity::class.java, id.toUUID())
        partnerEntity?.let { entityManager.remove(it) }
    }

    override fun deleteAll() {
        entityManager.createQuery("DELETE FROM orders").executeUpdate()
    }
}
