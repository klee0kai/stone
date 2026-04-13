package com.github.klee0kai.stone.weakref

/**
 * A multiplatform weak reference that implements [Ref] and [AutoCloseable].
 *
 * The referent may be garbage-collected at any time — [get] returns `null`
 * once the GC has reclaimed the object. This is useful for dependencies that
 * should not prevent garbage collection, such as caches or optional observers.
 *
 * Platform implementations delegate to:
 * - **JVM**: `java.lang.ref.WeakReference`
 * - **Native**: `kotlin.native.ref.WeakReference`
 * - **JS / WasmJS**: `WeakRef` (ES2021)
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface ITechProviderComponent {
 *     fun batteryWeak(): WeakRef<Battery?>?
 * }
 *
 * @Component
 * interface WireComponent {
 *     fun usb_usb(): WeakRef<Wire<Usb?, Usb?>?>?
 * }
 * ```
 *
 * ### Field and method injection
 *
 * ```kotlin
 * class Mowgli {
 *     @Inject
 *     var knowledgeWeakRef: WeakRef<Knowledge?>? = null
 *
 *     @Inject
 *     fun refInject(
 *         knowledgeWeakRef: WeakRef<Knowledge?>?,
 *         knowledgeSoftRef: SoftRef<Knowledge?>?
 *     ) {
 *         methodKnowledgeWeakRef = knowledgeWeakRef
 *     }
 * }
 * ```
 *
 * ### Module-level provideWrapper
 *
 * ```kotlin
 * @Module(genProviderName = "TechFactoryProviders")
 * interface TechFactoryGenProvideModule {
 *     @Named("null_args")
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = WeakRef::class)
 *     fun phoneOsNamed(osType: PhoneOsType?): OperationSystem?
 * }
 * ```
 *
 * ### Accessing the value
 *
 * ```kotlin
 * val di = TechFactoryComponentStoneComponent()
 * val batteryWeak = di.batteryWeak()
 *
 * // may return null if GC reclaimed the referent
 * val battery = batteryWeak?.get()
 * ```
 *
 * @param T the type of the weakly-referenced object
 * @param value the initial value to wrap in a weak reference
 * @see SoftRef
 * @see WeakRefDelegate
 * @see Ref
 */
expect class WeakRef<T : Any?> constructor(value: T) : Ref<T?>, AutoCloseable {

    /**
     * Returns the referent, or `null` if it has been garbage-collected.
     */
    override fun get(): T?

    /**
     * Clears the weak reference, making [get] return `null` regardless
     * of whether the referent is still alive.
     */
    fun clear()

    /**
     * Clears the weak reference. Equivalent to [clear].
     */
    override fun close()

    override fun hashCode(): Int

    override fun equals(other: Any?): Boolean

}