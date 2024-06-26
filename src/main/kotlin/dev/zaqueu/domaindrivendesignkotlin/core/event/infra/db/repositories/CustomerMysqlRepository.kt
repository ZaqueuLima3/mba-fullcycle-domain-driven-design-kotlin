package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories.CustomerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.CustomerEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.CustomerEntity.Companion.toDomain
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class CustomerMysqlRepository(
    private val entityManager: EntityManager
) : CustomerRepository {

    override fun add(entity: Customer) {
        entityManager.persist(CustomerEntity.fromDomain(entity))
    }

    override fun update(entity: Customer) {
        entityManager.merge(CustomerEntity.fromDomain(entity))
    }

    override fun findById(id: Uuid): Customer? {
        val entity = entityManager.find(CustomerEntity::class.java, id.toUUID())
        return entity?.toDomain()
    }

    override fun findAll(): List<Customer> {
        val query = entityManager.createQuery("SELECT c FROM customers c", CustomerEntity::class.java)
        val entities = query.resultList
        return entities.map { it.toDomain() }
    }

    override fun delete(id: Uuid) {
        val partnerEntity = entityManager.find(CustomerEntity::class.java, id.toUUID())
        partnerEntity?.let { entityManager.remove(it) }
    }

    override fun deleteAll() {
        entityManager.createQuery("DELETE FROM customers").executeUpdate()
    }
}
