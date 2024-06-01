package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Repository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.event.entities.Event

internal interface EventRepository : Repository<Event>