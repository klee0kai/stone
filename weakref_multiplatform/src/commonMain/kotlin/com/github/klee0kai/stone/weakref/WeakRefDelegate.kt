package com.github.klee0kai.stone.weakref

import kotlin.reflect.KProperty

/**
 * A Kotlin property delegate backed by a [WeakRef].
 *
 * Allows any property to hold its value via a weak reference, so the referent
 * can be garbage-collected when no other strong references exist. Reading the
 * property returns `null` once the GC has reclaimed the object; writing a
 * non-null value replaces the underlying [WeakRef].
 *
 * ### Basic usage
 *
 * ```kotlin
 * var presenter: WelcomePresenter? by WeakRefDelegate(WelcomePresenter())
 *
 * // reading — returns null if GC reclaimed the referent
 * val current = presenter
 *
 * // writing — replaces the weak reference
 * presenter = WelcomePresenter()
 *
 * // explicitly clearing the reference
 * // (use the delegate instance directly)
 * ```
 *
 * ### Typical use case
 *
 * `WeakRefDelegate` is useful for holding references to objects that have a
 * lifecycle managed elsewhere (e.g., UI presenters, activity contexts) and should
 * not be kept alive by the holder:
 *
 * ```kotlin
 * class Navigator {
 *     var currentScreen: Screen? by WeakRefDelegate(initialScreen)
 *
 *     fun navigate() {
 *         // currentScreen may be null if GC reclaimed it
 *         currentScreen?.show()
 *     }
 * }
 * ```
 *
 * @param T the type of the weakly-referenced object (must be non-null for initial value)
 * @param value the initial value to wrap in a [WeakRef]
 * @see WeakRef
 * @see Ref
 */
class WeakRefDelegate<T : Any>(value: T) {

    private var weakRef = WeakRef(value)

    /**
     * Returns the current value, or `null` if the referent has been
     * garbage-collected.
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakRef.get()
    }

    /**
     * Replaces the underlying [WeakRef] with a new one wrapping [value].
     * If [value] is `null`, the current reference is kept unchanged.
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (value != null) {
            weakRef = WeakRef(value)
        }
    }

    /**
     * Clears the underlying [WeakRef], causing subsequent reads to return `null`.
     */
    fun clear() {
        weakRef.clear()
    }
}