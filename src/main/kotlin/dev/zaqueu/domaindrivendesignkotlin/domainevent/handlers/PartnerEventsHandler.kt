package dev.zaqueu.domaindrivendesignkotlin.domainevent.handlers

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents.DomainEvent
import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents.DomainEventHandler
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.domainevents.partner.PartnerChangedNameEvent
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.domainevents.partner.PartnerCreatedEvent
import org.springframework.stereotype.Component

@Component
internal class PartnerEventsHandler : DomainEventHandler {
    val listenTo = listOf(
        "PartnerChangedNameEvent",
        "PartnerCreatedEvent",
    )

    override fun handle(event: DomainEvent) {
        when (event) {
            is PartnerCreatedEvent -> partnerCreatedHandle(event)
            is PartnerChangedNameEvent -> partnerChangedNameHandle(event)
        }
    }

    private fun partnerChangedNameHandle(event: PartnerChangedNameEvent) {
        val values = event::class.java.constructors.first().parameters
        val e = mutableMapOf<String, Any>()

        values.forEach { value ->
            val field = PartnerChangedNameEvent::class.java.declaredFields.find { value.name == it.name }
            field?.isAccessible = true
            e[value.name] = field!!.get(event)
        }

        println("partner changed event $e")
    }

    fun partnerCreatedHandle(event: PartnerCreatedEvent) {
        val values = event::class.java.constructors.first().parameters
        val e = mutableMapOf<String, Any>()

        values.forEach { value ->
            val field = PartnerCreatedEvent::class.java.declaredFields.find { value.name == it.name }
            field?.isAccessible = true
            e[value.name] = field!!.get(event)
        }

        println("partner created event $e")
    }
}
