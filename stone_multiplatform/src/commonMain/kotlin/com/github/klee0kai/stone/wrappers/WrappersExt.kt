package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.weakref.Ref
import kotlin.reflect.KProperty

/**
 * Enables Kotlin property delegation for [Ref].
 *
 * Allows using a `Ref<T>` as a delegated property with `by`:
 *
 * ```kotlin
 * val earth: Earth by DI.planets().earth()
 * ```
 *
 * Each property access calls [Ref.get] and returns a non-null value.
 *
 * @throws NullPointerException if [Ref.get] returns `null`
 */
operator fun <T> Ref<T>.getValue(t: Any?, property: KProperty<*>): T = get()!!

/**
 * Enables Kotlin property delegation for [Provider].
 *
 * Allows using a `Provider<T>` as a delegated property with `by`:
 *
 * ```kotlin
 * val battery: Battery by DI.battery()
 * ```
 *
 * Each property access calls [Provider.get] and returns a fresh or cached value
 * depending on the provider's caching strategy.
 */
operator fun <T> Provider<T>.getValue(t: Any?, property: KProperty<*>): T = get()

