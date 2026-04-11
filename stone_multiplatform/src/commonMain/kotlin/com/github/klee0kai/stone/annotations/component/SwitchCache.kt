package com.github.klee0kai.stone.annotations.component


/**
 * Changes the caching strategy for DI objects at runtime, temporarily or permanently.
 *
 * Must be combined with one or more **scope annotations** to define which
 * objects are affected. This allows you to either speed up cleanup of unused
 * objects (switch to [CacheType.Weak]) or protect them from deletion
 * (switch to [CacheType.Strong]).
 *
 * ---
 *
 * ## Permanent cache switch
 *
 * ```kotlin
 * @Component
 * interface SwitchCacheComponent {
 *     fun earth(): GcEarthModule?
 *
 *     @GcAllScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Weak)
 *     fun allWeak()
 *
 *     @GcStrongScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Weak)
 *     fun strongToWeak()
 * }
 * ```
 *
 * ---
 *
 * ## Temporary cache switch with timeout
 *
 * The [timeMillis] parameter limits how long the new strategy stays active.
 * After the timeout, the cache type is restored to its original configuration:
 *
 * ```kotlin
 * @Component
 * interface PlanetsComponent {
 *     fun sunModule(): PlanetsModule?
 *
 *     @SunScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
 *     fun protectSun()
 *
 *     @GcAllScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
 *     fun allStrongFewMillis()
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - When multiple scope annotations are combined, the final scope is their **intersection**.
 * - [CacheType.Default] restores the original caching configuration as declared in `@Provide`.
 * - [CacheType.Reset] clears the cached value entirely — the object will be re-created
 *   on the next access.
 * - A temporary switch (`timeMillis > 0`) is useful for protecting objects during
 *   screen transitions or activity re-creation.
 *
 * @see RunGc
 * @see GcAllScope
 * @see GcScopeAnnotation
 * @see ProtectInjected
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class SwitchCache(
    /**
     * The new cache type to apply to the targeted objects.
     *
     * Defaults to [CacheType.Default] which restores the original caching strategy.
     */
    val cache: CacheType = CacheType.Default,

    /**
     * Duration in milliseconds for the cache switch.
     *
     * After this time, the cache type is automatically restored to the default configuration.
     * A value of `-1` (default) means the switch is **permanent** until explicitly changed.
     *
     * ```kotlin
     * @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
     * fun protectTemporarily()
     * ```
     */
    val timeMillis: Long = -1
) {

    /**
     * Cache type options for runtime switching.
     */
    enum class CacheType {
        /** Restore the original caching strategy as declared in `@Provide`. */
        Default,

        /** Clear the cached value entirely. The object will be re-created on next access. */
        Reset,

        /** Switch to weak reference caching. Objects are reclaimed when no strong references remain. */
        Weak,

        /** Switch to soft reference caching. Objects are reclaimed under memory pressure. */
        Soft,

        /** Switch to strong reference caching. Objects are never reclaimed automatically. */
        Strong
    }
}
