package com.github.klee0kai.stone.wrappers

import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Enables Kotlin property delegation for [SoftReference] (JVM only).
 *
 * Allows using a `SoftReference<T>` as a delegated property with `by`:
 *
 * ```kotlin
 * val mountain: Mountain? by DI.mountainSoft()
 * ```
 *
 * Returns `null` if the GC has reclaimed the referent under memory pressure.
 */
operator fun <T> SoftReference<T>.getValue(t: Any?, property: KProperty<*>): T? = get()

/**
 * Enables Kotlin property delegation for [WeakReference] (JVM only).
 *
 * Allows using a `WeakReference<T>` as a delegated property with `by`:
 *
 * ```kotlin
 * val river: River? by DI.riverWeak()
 * ```
 *
 * Returns `null` if the GC has reclaimed the referent.
 */
operator fun <T> WeakReference<T>.getValue(t: Any?, property: KProperty<*>): T? = get()

