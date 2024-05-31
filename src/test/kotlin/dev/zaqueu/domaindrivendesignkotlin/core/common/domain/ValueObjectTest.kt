package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MockObjVO(value: String) : ValueObject<String>(value)

class ValueObjectTest {
    @Test
    fun `should compare the value of two different instances`() {
        val expectedValue = "any"

        val mock1 = MockObjVO(expectedValue)
        val mock2 = MockObjVO(expectedValue)

        Assertions.assertTrue(mock1 == mock2, "The two instances should be equal")
    }

    @Test
    fun `should have the same hash code for two different instances with the same value`() {
        val expectedValue = "any"

        val mock1 = MockObjVO(expectedValue)
        val mock2 = MockObjVO(expectedValue)

        Assertions.assertEquals(mock1.hashCode(), mock2.hashCode(), "The hash codes should be equal")
    }
}
