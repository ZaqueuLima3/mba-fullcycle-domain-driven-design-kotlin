package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.Uuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.PartnerEntity
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.PartnerEntity.Companion.toDomain
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import java.util.UUID

@Component
internal class PartnerMysqlRepository(
    private val entityManager: EntityManager
) : PartnerRepository {

    override fun add(entity: Partner) {
        entityManager.persist(PartnerEntity.fromDomain(entity))
    }

    override fun update(entity: Partner) {
        entityManager.merge(PartnerEntity.fromDomain(entity))
    }

    override fun findById(id: Uuid): Partner? {
        val entity = entityManager.find(PartnerEntity::class.java, id.toUUID())
        return entity?.toDomain()
    }

    override fun findAll(): List<Partner> {
        val query = entityManager.createQuery("SELECT p FROM partners p", PartnerEntity::class.java)
        val entities = query.resultList
        return entities.map { it.toDomain() }
    }

    override fun delete(id: Uuid) {
        val partnerEntity = entityManager.find(PartnerEntity::class.java, id.toUUID())
        partnerEntity?.let { entityManager.remove(it) }
    }

    override fun deleteAll() {
        entityManager.createQuery("DELETE FROM partners").executeUpdate()
    }
}
