package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.repositories

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities.PartnerEntity
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class PartnerMysqlRepositoryTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var partnerRepository: PartnerRepository

    @Test
    @Transactional
    fun `should add a new partner`() {
        val partner = Partner.create(
            name = "Test Partner"
        )

        partnerRepository.add(partner)
        entityManager.flush()
        entityManager.clear()

        val partnerEntity = entityManager.find(PartnerEntity::class.java, partner.id.toUUID())
        Assertions.assertNotNull(partnerEntity)
        Assertions.assertEquals(partner.name, partnerEntity.name)
    }

    @Test
    @Transactional
    fun `should update a partner`() {
        val partner = Partner.create(
            name = "Test Partner"
        )

        partnerRepository.add(partner)
        entityManager.flush()
        entityManager.clear()

        var partnerEntity = entityManager.find(PartnerEntity::class.java, partner.id.toUUID())
        Assertions.assertNotNull(partnerEntity)
        Assertions.assertEquals(partner.name, partnerEntity.name)

        partner.changeName("New Name")
        partnerRepository.update(partner)
        entityManager.flush()
        entityManager.clear()

        partnerEntity = entityManager.find(PartnerEntity::class.java, partner.id.toUUID())
        Assertions.assertNotNull(partnerEntity)
        Assertions.assertEquals(partner.name, partnerEntity.name)
    }

    @Test
    @Transactional
    fun `should return a partner when it is found`() {
        val partner = Partner.create(
            name = "Test Partner"
        )

        partnerRepository.add(partner)
        entityManager.flush()
        entityManager.clear()

        val savedPartner = partnerRepository.findById(partner.id)
        Assertions.assertNotNull(savedPartner)
        Assertions.assertEquals(partner.name, savedPartner?.name)
    }

    @Test
    fun `should return null when partner is no found`() {
        val savedPartner = partnerRepository.findById(UUID.randomUUID().toDomainUuid<PartnerId>())
        Assertions.assertNull(savedPartner)
    }

    @Test
    @Transactional
    fun `should return a list of partners`() {
        val partner1 = Partner.create(
            name = "Test Partner one"
        )
        val partner2 = Partner.create(
            name = "Test Partner two"
        )

        partnerRepository.add(partner1)
        partnerRepository.add(partner2)
        entityManager.flush()
        entityManager.clear()

        val partners = partnerRepository.findAll()

        Assertions.assertTrue(partners.size == 2)
        Assertions.assertTrue(partners.contains(partner1))
        Assertions.assertTrue(partners.contains(partner2))
    }

    @Test
    @Transactional
    fun `should return an empty list of partners`() {
        val partners = partnerRepository.findAll()
        Assertions.assertTrue(partners.isEmpty())
    }

    @Test
    @Transactional
    fun `should delete a partner when it is found`() {
        val partner = Partner.create(
            name = "Test Partner"
        )

        partnerRepository.add(partner)
        entityManager.flush()
        entityManager.clear()

        val savedPartner = entityManager.find(PartnerEntity::class.java, partner.id.toUUID())
        Assertions.assertNotNull(savedPartner)
        Assertions.assertEquals(partner.name, savedPartner?.name)

        partnerRepository.delete(partner.id)

        val deletedPartner = entityManager.find(PartnerEntity::class.java, partner.id.toUUID())

        Assertions.assertNull(deletedPartner)
    }

    @Test
    fun `should do nothing when doesn't find a partner to delete`() {
        Assertions.assertDoesNotThrow {
            partnerRepository.delete(UUID.randomUUID().toDomainUuid<PartnerId>())
        }
    }
}
