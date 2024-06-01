package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Repository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.entities.event.Event

internal interface EventRepository : Repository<Event>