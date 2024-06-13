package dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents.DomainEventManager
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.valueobjects.toDomainUuid
import dev.zaqueu.domaindrivendesignkotlin.core.common.exceptions.ResourceNotFoundException
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.CreatePartnerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.CreatePartnerDto.Companion.toDomain
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.UpdatePartnerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.valueobject.PartnerId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class PartnerService(
    private val partnerRepository: PartnerRepository,
    private val domainEventManager: DomainEventManager,
) {
    @Transactional
    fun list(): List<Partner> {
        return partnerRepository.findAll()
    }

    @Transactional
    fun register(input: CreatePartnerDto): Partner {
        val partner = input.toDomain()
        partnerRepository.add(partner)
        domainEventManager.publish(partner)
        return partner
    }

    @Transactional
    fun findById(id: String): Partner? {
        return partnerRepository.findById(id.toDomainUuid<PartnerId>())
    }


    @Transactional
    fun update(input: UpdatePartnerDto): Partner {
        val partner = partnerRepository.findById(input.id.toDomainUuid<PartnerId>())
            ?: throw ResourceNotFoundException("Partner with id ${input.id} not found")

        if (!input.name.isNullOrBlank()) partner.changeName(input.name)

        partnerRepository.update(partner)
        domainEventManager.publish(partner)

        return partner
    }

    @Transactional
    fun delete(id: String) {
        partnerRepository.delete(id.toDomainUuid<PartnerId>())
    }
}
