package dev.zaqueu.domaindrivendesignkotlin.api.partner.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.UpdatePartnerDto
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdatePartnerRequest(
    val name: String,
) {
    fun toInput(id: String) = UpdatePartnerDto(
        id = id,
        name = name,
    )
}
