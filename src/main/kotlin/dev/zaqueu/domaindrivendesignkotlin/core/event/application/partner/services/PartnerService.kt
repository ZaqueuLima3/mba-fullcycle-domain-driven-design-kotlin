package dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.CreatePartnerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.CreatePartnerDto.Companion.toDomain
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.UpdatePartnerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.toPartnerId
import org.springframework.stereotype.Service

@Service
internal class PartnerService(
    private val partnerRepository: PartnerRepository,
    private val unitOfWork: UnitOfWork
) {
    fun list(): List<Partner> {
        return partnerRepository.findAll()
    }

    fun register(input: CreatePartnerDto): Partner {
        val partner = input.toDomain()
        partnerRepository.add(partner)
        unitOfWork.commit()
        return partner
    }

    fun update(input: UpdatePartnerDto): Partner {
        val partner = partnerRepository.findById(input.id.toPartnerId()) ?: throw Exception("Partner not found")

        if (!input.name.isNullOrBlank()) partner.changeName(input.name)

        partnerRepository.add(partner)
        unitOfWork.commit()

        return partner
    }
}
