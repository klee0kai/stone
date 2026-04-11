package com.github.klee0kai.stone.annotations.component

/**
 * Meta-annotation for declaring custom GC scope annotations.
 *
 * Custom scope annotations allow you to group cached objects and selectively
 * target them for garbage collection ([RunGc]) or cache switching ([SwitchCache]).
 * Apply this annotation to your custom annotation class to register it as a GC scope.
 *
 * ---
 *
 * ## Declaring a custom scope
 *
 * ```kotlin
 * @GcScopeAnnotation
 * @Retention(AnnotationRetention.RUNTIME)
 * @Target(AnnotationTarget.FUNCTION)
 * annotation class GcMercuryScope
 *
 * @GcScopeAnnotation
 * @Retention(AnnotationRetention.RUNTIME)
 * @Target(AnnotationTarget.FUNCTION)
 * annotation class SunScope
 * ```
 *
 * ---
 *
 * ## Using custom scopes in modules and components
 *
 * ```kotlin
 * @Module
 * interface PlanetsModule {
 *     @GcMercuryScope
 *     @BindInstance
 *     fun mercury(mercury: Mercury? = null): Mercury?
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun venus(): Venus?
 * }
 *
 * @Component
 * interface PlanetsComponent {
 *     fun sunModule(): PlanetsModule?
 *
 *     @RunGc @GcMercuryScope
 *     fun gcMercury()
 *
 *     @SunScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
 *     fun protectSun()
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - Alternatively, the `@Scope` annotation from `javax.inject` / `com.github.klee0kai.stone`
 *   can be used instead of `@GcScopeAnnotation` to mark scope annotations.
 * - Custom scopes can be stacked — multiple scope annotations on a single method
 *   create an **intersection**: only objects matching all specified scopes are affected.
 * - Scope annotations can be applied to both `@Provide` methods (in modules) and
 *   `@RunGc`/`@SwitchCache` methods (in components).
 *
 * @see RunGc
 * @see SwitchCache
 * @see GcAllScope
 * @see GcStrongScope
 * @see GcSoftScope
 * @see GcWeakScope
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class GcScopeAnnotation 
