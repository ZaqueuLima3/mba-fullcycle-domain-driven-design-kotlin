package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.IntegrationTest
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureTestEntityManager
@IntegrationTest
class PartnerEntityTest {

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    @Transactional
    fun `should persist and retrieve PartnerEntity`() {
        val partnerEntity = PartnerEntity(
            id = UUID.randomUUID(),
            name = "Test Partner"
        )

        testEntityManager.persist(partnerEntity)
        entityManager.flush()
        entityManager.clear()

        val foundPartnerEntity = entityManager.find(PartnerEntity::class.java, partnerEntity.id)
        Assertions.assertNotNull(foundPartnerEntity)
        Assertions.assertEquals(partnerEntity.name, foundPartnerEntity.name)
    }
}
