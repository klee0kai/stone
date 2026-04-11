package com.github.klee0kai.stone.weakref

/**
 * Base functional interface for providing instances of [T].
 *
 * `Ref` is the foundational contract in the Stone DI framework — all wrapper types
 * ([WeakRef], [SoftRef], [com.github.klee0kai.stone.wrappers.LazyProvider]) implement or
 * accept `Ref` as a source of values. It can also be used directly as a lightweight
 * factory or provider in components, modules, and injection targets.
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface ITechProviderComponent {
 *     fun batteryProviderIRef(): Ref<Battery?>?
 * }
 * ```
 *
 * ### Field and method injection
 *
 * ```kotlin
 * class Mowgli {
 *     @Inject
 *     var knowledgePhantomProvide2: Ref<Knowledge?>? = null
 *
 *     @Inject
 *     fun wrapperInject(knowledgePhantomProvide2: Ref<Knowledge?>?) {
 *         methodKnowledgePhantomProvide2 = knowledgePhantomProvide2
 *     }
 * }
 *
 * class CarInjectProvider {
 *     @Inject
 *     var wheel: Ref<Wheel>? = null
 *
 *     @Inject
 *     var window: Ref<CarLazy<Window>>? = null
 *
 *     @Inject
 *     fun init(wheel: Ref<Wheel>, window: Ref<CarLazy<Window>>) { ... }
 * }
 * ```
 *
 * ### Creating Ref instances in a module
 *
 * ```kotlin
 * @Module
 * open class CarInjectModule {
 *     open fun passengerWindows(): List<Ref<Window>> {
 *         return listOf(Ref<Window> { Window() }, Ref<Window> { Window() })
 *     }
 * }
 * ```
 *
 * ### Module-level provideWrapper
 *
 * ```kotlin
 * @Module(genProviderName = "TechFactoryProviders")
 * interface TechFactoryGenProvideModule {
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = Ref::class)
 *     fun ram(ramSize: RamSize? = null): Ram?
 * }
 * ```
 *
 * ### Binding instances via Ref
 *
 * ```kotlin
 * @Component
 * interface CarBindComponent {
 *     fun bindBumper(bumper: Ref<Bumper>?)
 *     fun provideBumper(): Ref<Bumper?>?
 * }
 * ```
 *
 * ### Using as a Kotlin delegated property
 *
 * With the extension from `WrappersExt`:
 *
 * ```kotlin
 * val battery: Battery by di.batteryProviderIRef()!!
 * ```
 *
 * ### Building custom wrappers on top of Ref
 *
 * ```kotlin
 * class CustomLazy<T>(call: Ref<T>) {
 *     private val weakRef: WeakReference<T> = WeakReference(call.get())
 *     val value: T? get() = weakRef.get()
 * }
 *
 * class CustomStoneProvide<T>(val call: Ref<T>) {
 *     fun get(): T = call.get()
 * }
 * ```
 *
 * @param T the type of object this reference supplies
 * @see WeakRef
 * @see SoftRef
 * @see com.github.klee0kai.stone.wrappers.LazyProvider
 * @see com.github.klee0kai.stone.Provider
 */
fun interface Ref<T> {

    /**
     * Returns an instance of [T].
     *
     * Depending on the implementation, the returned value may be a new instance,
     * a cached value, or `null` (for [WeakRef] / [SoftRef] when the referent
     * has been garbage-collected).
     */
    fun get(): T

}