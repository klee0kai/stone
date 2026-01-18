package com.github.klee0kai.thekey.stone.ksp.property

import kotlin.reflect.KProperty

class Property<T>(
    initValue: T
) {
    companion object;

    private val subscriptions = mutableListOf<(T) -> Unit>()

    var value: T = initValue
        set(value) {
            field = value
            subscriptions.forEach { it.invoke(value) }
        }

    private val updateThisValueSubscription: (T) -> Unit = { value = it }

    var source: Property<T>? = null
        set(value) {
            field?.unsubscribeChanges(updateThisValueSubscription)
            field = value
            field?.subscribeChanges(updateThisValueSubscription)
        }

    fun subscribeChanges(
        block: (T) -> Unit,
    ) {
        if (block === updateThisValueSubscription) {
            return
        }
        subscriptions += block
        block(value)
    }

    fun unsubscribeChanges(block: (T) -> Unit) {
        subscriptions -= block
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T = value

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        newValue: T,
    ) {
        value = newValue
    }

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        newValue: Property<T>,
    ) {
        source = newValue
    }

}

fun <T, R> Property<T>.map(
    transform: (T) -> R,
): Property<R> {
    val prop = Property<R>(transform(value))
    subscribeChanges { prop.value = transform(it) }
    return prop
}

