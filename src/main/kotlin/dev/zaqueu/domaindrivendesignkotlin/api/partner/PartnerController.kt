package dev.zaqueu.domaindrivendesignkotlin.api.partner

import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.CreatePartnerRequest
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.PartnerListResponse
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.PartnerResponse
import dev.zaqueu.domaindrivendesignkotlin.api.partner.models.UpdatePartnerRequest
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.services.PartnerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
internal class PartnerController(
    private val partnerService: PartnerService
) : PartnerApi {

    override fun createPartner(request: CreatePartnerRequest): ResponseEntity<PartnerResponse> {
        val partner = partnerService.register(request.toInput())
        return ResponseEntity
            .created(URI.create("/partners/${partner.id.value}"))
            .body(PartnerResponse.fromDomain(partner))
    }

    override fun updateById(id: String, request: UpdatePartnerRequest): ResponseEntity<PartnerResponse> {
        val partner = partnerService.update(request.toInput(id))
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(PartnerResponse.fromDomain(partner))
    }

    override fun listPartners(): ResponseEntity<List<PartnerListResponse>> {
        val partners = partnerService.list()
        val partnerResponse = partners.map { PartnerListResponse.fromDomain(it) }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(partnerResponse)
    }

    override fun getById(id: String): ResponseEntity<PartnerResponse?> {
        val partner = partnerService.findById(id) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok()
            .body(PartnerResponse.fromDomain(partner))
    }

    override fun deleteById(id: String) {
        partnerService.delete(id)
    }
}
