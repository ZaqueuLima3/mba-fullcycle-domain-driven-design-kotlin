package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Repository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.order.entities.Order

internal interface OrderRepository : Repository<Order>
