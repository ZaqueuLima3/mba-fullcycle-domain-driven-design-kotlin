package dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner

internal data class CreatePartnerDto(
    val name: String,
) {
    companion object {
        fun CreatePartnerDto.toDomain(): Partner {
            return Partner.create(
                name = name,
            )
        }
    }
}
