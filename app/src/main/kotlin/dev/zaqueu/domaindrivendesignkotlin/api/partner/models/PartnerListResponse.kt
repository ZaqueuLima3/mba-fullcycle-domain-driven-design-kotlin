package dev.zaqueu.domaindrivendesignkotlin.api.partner.models

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import kotlinx.serialization.Serializable

@Serializable
internal data class PartnerListResponse(
    val id: String,
    val name: String,
) {
    companion object {
        fun fromDomain(partner: Partner) = PartnerListResponse(
            id = partner.id.value,
            name = partner.name,
        )
    }
}
