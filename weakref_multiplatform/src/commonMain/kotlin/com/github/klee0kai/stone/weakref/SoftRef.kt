package com.github.klee0kai.stone.weakref

/**
 * A multiplatform soft reference that implements [Ref] and [AutoCloseable].
 *
 * Unlike [WeakRef], a soft reference is only cleared by the GC when memory
 * is under pressure, making it suitable for memory-sensitive caches where
 * the value should survive as long as there is enough heap space.
 *
 * Platform implementations delegate to:
 * - **JVM**: `java.lang.ref.SoftReference`
 * - **Native**: `kotlin.native.ref.WeakReference` (Kotlin/Native has no soft references;
 *   falls back to weak reference semantics)
 * - **JS / WasmJS**: `WeakRef` (ES2021; no soft reference semantics in JS engines)
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface ITechProviderComponent {
 *     fun batterySoft(): SoftRef<Battery?>?
 * }
 * ```
 *
 * ### Field and method injection
 *
 * ```kotlin
 * class Mowgli {
 *     @Inject
 *     var knowledgeSoftRef: SoftRef<Knowledge?>? = null
 *
 *     @Inject
 *     fun refInject(
 *         knowledgeWeakRef: WeakRef<Knowledge?>?,
 *         knowledgeSoftRef: SoftRef<Knowledge?>?
 *     ) {
 *         methodKnowledgeSoftRef = knowledgeSoftRef
 *     }
 * }
 * ```
 *
 * ### Accessing the value
 *
 * ```kotlin
 * val di = TechFactoryComponentStoneComponent()
 * val batterySoft = di.batterySoft()
 *
 * // may return null under memory pressure
 * val battery = batterySoft?.get()
 * ```
 *
 * ### Internal usage in Stone holders
 *
 * Stone's internal `TimeHolder` uses `SoftRef` to cache provided objects
 * so they survive as long as memory allows:
 *
 * ```kotlin
 * // inside TimeHolder
 * this.ref = SoftRef(ob)
 * ```
 *
 * @param T the type of the softly-referenced object
 * @param value the initial value to wrap in a soft reference
 * @see WeakRef
 * @see Ref
 */
expect class SoftRef<T : Any?> constructor(value: T) : Ref<T?>, AutoCloseable {

    /**
     * Returns the referent, or `null` if the GC has cleared it
     * due to memory pressure.
     */
    override fun get(): T?

    /**
     * Clears the soft reference, making [get] return `null` regardless
     * of memory conditions.
     */
    fun clear()

    /**
     * Clears the soft reference. Equivalent to [clear].
     */
    override fun close()

}