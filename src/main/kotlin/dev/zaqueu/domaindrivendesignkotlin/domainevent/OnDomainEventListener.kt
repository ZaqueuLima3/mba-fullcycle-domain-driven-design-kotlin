package dev.zaqueu.domaindrivendesignkotlin.domainevent

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.domainevents.DomainEventManager
import dev.zaqueu.domaindrivendesignkotlin.domainevent.handlers.PartnerEventsHandler
import org.springframework.stereotype.Component

@Component
internal class OnDomainEventListener(
    private val eventManager: DomainEventManager,
    private val partnerEventsHandler: PartnerEventsHandler,
) {

    init {
        partnerEventsHandler.listenTo.forEach { eventName ->
            eventManager.register(eventName, partnerEventsHandler::handle)
        }
    }
}
