package dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.repositories

import dev.zaqueu.domaindrivendesignkotlin.core.common.domain.Repository
import dev.zaqueu.domaindrivendesignkotlin.core.event.domain.customer.entities.Customer

internal interface CustomerRepository : Repository<Customer>
