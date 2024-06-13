package dev.zaqueu.domaindrivendesignkotlin.core.common.domain

internal abstract class ValueObject<Value : Any>(private val _value: Value) {
    val value: Value
        get() = this._value

    override fun equals(other: Any?): Boolean {
        val obj = other as? ValueObject<*> ?: return false

        return obj.value == this.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
