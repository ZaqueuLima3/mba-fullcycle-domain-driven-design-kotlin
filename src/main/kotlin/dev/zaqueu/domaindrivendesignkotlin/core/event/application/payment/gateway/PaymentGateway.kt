package dev.zaqueu.domaindrivendesignkotlin.core.event.application.payment.gateway

import org.springframework.stereotype.Component

@Component
class PaymentGateway {
    fun payment(token: String, amount: Long) {

    }
}
