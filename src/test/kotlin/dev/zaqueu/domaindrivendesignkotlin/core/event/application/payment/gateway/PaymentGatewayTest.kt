package dev.zaqueu.domaindrivendesignkotlin.core.event.application.payment.gateway

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PaymentGatewayTest {
    private val paymentGateway = PaymentGateway()

    @Test
    fun `payment test`() {
        Assertions.assertDoesNotThrow {
            paymentGateway.payment("token", 1000)
        }
    }
}
