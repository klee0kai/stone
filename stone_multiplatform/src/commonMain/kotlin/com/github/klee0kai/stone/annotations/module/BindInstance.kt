package com.github.klee0kai.stone.annotations.module


/**
 * Binds an already-existing object into the DI graph.
 *
 * Unlike `@Provide`, which creates new instances, `@BindInstance` is used for objects
 * that are created **outside** of DI and need to be made available as dependencies.
 * Can be declared on methods in both modules and components.
 *
 * ---
 *
 * ## Binding in a module
 *
 * A module method annotated with `@BindInstance` declares a binding slot.
 * The method takes an optional parameter to set the value and returns the bound object:
 *
 * ```kotlin
 * @Module
 * interface SunSystemModule {
 *     @BindInstance(cache = BindInstance.CacheType.Weak)
 *     fun sun(sun: Sun? = null): Sun
 * }
 * ```
 *
 * ---
 *
 * ## Binding in a component
 *
 * **Bind-and-provide** — a single method both sets and retrieves the bound value:
 *
 * ```kotlin
 * @Component
 * interface PlanetComponent {
 *     fun sunModule(): SunSystemModule?
 *
 *     @BindInstance
 *     fun planet(planet: IPlanet?): IPlanet?
 *
 *     @BindInstance(cache = BindInstance.CacheType.Weak)
 *     fun earth(earth: Earth?): Earth?
 * }
 * ```
 *
 * **Bind-only** — a `void` (Unit) return type means the method only sets the value,
 * without providing it back:
 *
 * ```kotlin
 * @Component
 * interface SpaceComponent {
 *     fun sunSystem(): SunSystemModule
 *
 *     @BindInstance
 *     fun bindSun(sun: Sun?)
 * }
 * ```
 *
 * The bound object is then available through the module or a separate providing method:
 *
 * ```kotlin
 * @Component
 * interface SpaceComponent {
 *     fun sunSystem(): SunSystemModule
 *
 *     @BindInstance
 *     fun bindSun(sun: Sun?)
 *
 *     fun provideSun(): Sun?
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - If `null` is passed as an argument, the binding is **not cleared** — the
 *   previously bound value is returned unchanged. This is the "provide" behavior.
 * - Binding **does not support nulling** (setting the value to `null`). Once bound,
 *   the value can only be replaced by a new non-null instance. Use different
 *   caching methods ([CacheType.Weak]) to allow GC to reclaim the object.
 * - Unlike `@Provide`, `@BindInstance` does not support `CacheType.Factory`
 *   because a factory implies creating new objects, which contradicts binding semantics.
 *
 * @see Provide
 * @see Module
 * @see com.github.klee0kai.stone.annotations.component.Component
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class BindInstance(
    /**
     * Caching strategy for the bound object.
     *
     * Defaults to [CacheType.Soft] — the object is retained until the JVM
     * reclaims it under memory pressure.
     *
     * ```kotlin
     * @BindInstance(cache = BindInstance.CacheType.Weak)
     * fun sun(sun: Sun? = null): Sun
     *
     * @BindInstance(cache = BindInstance.CacheType.Strong)
     * fun earth(earth: Earth? = null): Earth
     * ```
     */
    val cache: CacheType = CacheType.Soft
) {

    /**
     * Caching strategies for bound objects.
     */
    enum class CacheType {
        /** The object is cached via a weak reference. Reclaimed when no strong references remain. */
        Weak,

        /** The object is cached via a soft reference. Reclaimed under memory pressure. */
        Soft,

        /** The object is cached via a strong reference. Never reclaimed automatically. */
        Strong
    }
}
