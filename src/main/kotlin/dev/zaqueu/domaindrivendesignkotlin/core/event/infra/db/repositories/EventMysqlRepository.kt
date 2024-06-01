package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventEntity.Companion.toDomain
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.PartnerEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class EventMysqlRepository(
    private val entityManager: EntityManager
) : EventRepository {

    override fun add(entity: Event) {
        val partner = entityManager.find(PartnerEntity::class.java, UUID.fromString(entity.partnerId.value))
        entityManager.persist(EventEntity.fromDomain(entity, partner))
    }

    override fun findById(id: Uuid): Event? {
        val entity = entityManager.find(EventEntity::class.java, UUID.fromString(id.value))
        return entity?.toDomain()
    }

    override fun findAll(): List<Event> {
        val query = entityManager.createQuery("SELECT c FROM events c", EventEntity::class.java)
        val entities = query.resultList
        return entities.map { it.toDomain() }
    }

    override fun delete(id: Uuid) {
        val partnerEntity = entityManager.find(EventEntity::class.java, UUID.fromString(id.value))
        partnerEntity?.let { entityManager.remove(it) }
    }
}
