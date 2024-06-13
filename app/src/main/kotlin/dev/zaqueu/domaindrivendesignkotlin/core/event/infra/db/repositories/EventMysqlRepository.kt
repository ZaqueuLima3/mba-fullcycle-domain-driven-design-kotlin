package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories.EventRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventEntity.Companion.toDomain
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSectionEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.EventSpotEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.PartnerEntity
import jakarta.persistence.EntityExistsException
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class EventMysqlRepository(
    private val entityManager: EntityManager,
) : EventRepository {

    private fun getPartner(partnerId: UUID): PartnerEntity {
        return entityManager.find(PartnerEntity::class.java, partnerId)
    }

    override fun add(entity: Event) {
        val partner = getPartner(entity.partnerId.toUUID())
        entityManager.persist(EventEntity.fromDomain(entity, partner))
    }

    override fun update(entity: Event) {
        val partner = getPartner(entity.partnerId.toUUID())
        entityManager.merge(EventEntity.fromDomain(entity, partner))
    }

    override fun findById(id: Uuid): Event? {
        val entity = entityManager.find(EventEntity::class.java, id.toUUID())
        return entity?.toDomain()
    }

    override fun findAll(): List<Event> {
        val query = entityManager.createQuery("SELECT e FROM events e", EventEntity::class.java)
        val entities = query.resultList
        return entities.map { it.toDomain() }
    }

    override fun delete(id: Uuid) {
        val partnerEntity = entityManager.find(EventEntity::class.java, id.toUUID())
        partnerEntity?.let { entityManager.remove(it) }
    }

    override fun deleteAll() {
        entityManager.createQuery("DELETE FROM events").executeUpdate()
    }
}
