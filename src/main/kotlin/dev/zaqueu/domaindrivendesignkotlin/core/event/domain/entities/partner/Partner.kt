package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.partner

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.AggregateRoot

internal data class Partner(
    override val id: PartnerId,
    val name: String,
) : AggregateRoot() {

    constructor(
        id: String? = null,
        name: String,
    ) : this(
        id = if (id != null) PartnerId(id) else PartnerId(),
        name = name,
    )

    companion object {
        fun create(name: String): Partner {
            return Partner(name = name)
        }
    }
}
