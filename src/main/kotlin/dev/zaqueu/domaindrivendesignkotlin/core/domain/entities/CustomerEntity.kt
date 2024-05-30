package dev.zaqueu.domaindrivendesignkotlin.core.domain.entities

internal data class CustomerEntity(
    val id: String,
    val cpf: String,
    val name: String,
)
