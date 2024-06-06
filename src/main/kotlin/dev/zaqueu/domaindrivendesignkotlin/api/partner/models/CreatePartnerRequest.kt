package dev.zaqueu.domaindrivendesignkotlin.api.partner.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.CreatePartnerDto
import kotlinx.serialization.Serializable

@Serializable
internal data class CreatePartnerRequest(
    val name: String,
) {
    fun toInput() =  CreatePartnerDto(
        name = name,
    )
}
