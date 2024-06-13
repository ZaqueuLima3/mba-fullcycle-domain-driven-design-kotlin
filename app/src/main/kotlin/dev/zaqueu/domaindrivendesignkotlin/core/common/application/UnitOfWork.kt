package dev.zaqueu.domaindrivendesignkotlin.core.common.application

internal interface UnitOfWork {
    fun commit()
    fun rollback()
}
