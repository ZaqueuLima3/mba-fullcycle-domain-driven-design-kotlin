package dev.zaqueu.domaindrivendesignkotlin.core.event.infra.db.entities

import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.partner.entities.Partner
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity(name = "partners")
@Table(name = "partners")
internal class PartnerEntity(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "name")
    var name: String
) {
    companion object {
        fun fromDomain(partner: Partner): PartnerEntity {
            return PartnerEntity(
                id = partner.id.toUUID(),
                name = partner.name,
            )
        }

        fun PartnerEntity.toDomain(): Partner {
            return Partner(
                id = id.toString(),
                name = name
            )
        }
    }
}
