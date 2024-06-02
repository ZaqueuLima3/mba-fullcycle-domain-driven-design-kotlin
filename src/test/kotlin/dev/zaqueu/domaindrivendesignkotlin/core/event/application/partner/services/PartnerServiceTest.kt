package dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.services

import dev.zaqueu.domaindrivendesignkotlin.core.common.application.UnitOfWork
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.CreatePartnerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.application.partner.dto.UpdatePartnerDto
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.repositories.PartnerRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class PartnerServiceTest {
    @MockK
    internal lateinit var partnerRepository: PartnerRepository

    @MockK
    internal lateinit var unitOfWork: UnitOfWork

    private lateinit var partnerService: PartnerService

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        partnerService = PartnerService(partnerRepository, unitOfWork)

        every {
            partnerRepository.add(any())
        } just Runs

        every {
            unitOfWork.commit()
        } just Runs
    }

    @Test
    fun `should list all partners`() {
        val expectedPartners = listOf(
            Partner.create(
                name = "John Doe",
            )
        )

        every {
            partnerRepository.findAll()
        } returns expectedPartners

        val partners = partnerService.list()

        Assertions.assertEquals(expectedPartners, partners)

        verify {
            partnerRepository.findAll()
        }
        confirmVerified(partnerRepository)
    }

    @Test
    fun `should register a new partner`() {
        val input = CreatePartnerDto(
            name = "Disney Plus",
        )

        val partner = partnerService.register(input)

        Assertions.assertNotNull(partner.id)
        Assertions.assertEquals(input.name, partner.name)

        verify {
            partnerRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(partnerRepository, unitOfWork)
    }

    @Test
    fun `should update the partner name`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdatePartnerDto(
            id = expectedId,
            name = "Disney Plus",
        )

        val partner = Partner(
            id = expectedId,
            name = "Disney",
        )

        every {
            partnerRepository.findById(partner.id)
        } returns partner

        val updatedPartner = partnerService.update(input)

        Assertions.assertEquals(input.name, updatedPartner.name)

        verify {
            partnerRepository.findById(partner.id)
            partnerRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(partnerRepository, unitOfWork)
    }

    @Test
    fun `should not update the partner name whe it is empty or null`() {
        val expectedId = UUID.randomUUID().toString()
        val input = UpdatePartnerDto(
            id = expectedId,
            name = null,
        )

        val partner = Partner(
            id = expectedId,
            name = "Disney",
        )

        every {
            partnerRepository.findById(partner.id)
        } returns partner

        val updatedPartner = partnerService.update(input)

        Assertions.assertEquals(partner.name, updatedPartner.name)

        verify {
            partnerRepository.findById(partner.id)
            partnerRepository.add(any())
            unitOfWork.commit()
        }
        confirmVerified(partnerRepository, unitOfWork)
    }
}
