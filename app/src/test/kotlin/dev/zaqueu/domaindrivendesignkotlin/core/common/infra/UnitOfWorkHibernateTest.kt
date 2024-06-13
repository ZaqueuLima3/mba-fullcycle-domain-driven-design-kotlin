package dev.zaqueu.domaindrivendesignkotlin.core.common.infra

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import io.mockk.*
import io.mockk.impl.annotations.MockK
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UnitOfWorkHibernateTest {

    @MockK
    private lateinit var entityManager: EntityManager

    private lateinit var unitOfWork: UnitOfWork

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        unitOfWork = UnitOfWorkHibernate(entityManager)
    }

    @Test
    fun `should call hibernate flush when calls commit`() {
        every {
            entityManager.flush()
        } just Runs

        unitOfWork.commit()

        verify {
            entityManager.flush()
        }
        confirmVerified(entityManager)
    }

    @Test
    fun `should call hibernate clean when calls rollback`() {
        every {
            entityManager.clear()
        } just Runs

        unitOfWork.rollback()

        verify {
            entityManager.clear()
        }
        confirmVerified(entityManager)
    }
}
