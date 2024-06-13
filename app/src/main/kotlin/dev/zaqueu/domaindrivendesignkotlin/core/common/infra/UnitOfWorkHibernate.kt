package dev.zaqueu.domaindrivendesignkotlin.core.common.infra

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
internal class UnitOfWorkHibernate(
    private val entityManager: EntityManager
) : UnitOfWork {
    override fun commit() {
        entityManager.flush()
    }

    override fun rollback() {
        entityManager.clear()
    }
}
