package com.github.klee0kai.stone.annotations.component


/**
 * Triggers explicit garbage collection for cached DI objects within the specified scopes.
 *
 * A method annotated with `@RunGc` must also be annotated with one or more
 * **scope annotations** ([GcAllScope], [GcStrongScope], [GcSoftScope], [GcWeakScope],
 * or custom scopes annotated with [GcScopeAnnotation]) to define which objects to target.
 *
 * ---
 *
 * ## Basic GC methods
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent {
 *     abstract fun sunSystem(): GcSunSystemModule?
 *     abstract fun earth(): GcEarthModule?
 *
 *     @RunGc @GcAllScope
 *     abstract fun gcAll()
 *
 *     @RunGc @GcStrongScope
 *     abstract fun gcStrong()
 *
 *     @RunGc @GcSoftScope
 *     abstract fun gcSoft()
 *
 *     @RunGc @GcWeakScope
 *     abstract fun gcWeak()
 * }
 * ```
 *
 * ---
 *
 * ## Custom GC scopes
 *
 * Define a custom scope and apply it to both module providers and GC methods:
 *
 * ```kotlin
 * @GcScopeAnnotation
 * @Retention(AnnotationRetention.RUNTIME)
 * @Target(AnnotationTarget.FUNCTION)
 * annotation class GcMercuryScope
 *
 * @Module
 * interface PlanetsModule {
 *     @GcMercuryScope
 *     @BindInstance
 *     fun mercury(mercury: Mercury? = null): Mercury?
 * }
 *
 * @Component
 * interface PlanetsComponent {
 *     fun sunModule(): PlanetsModule?
 *
 *     @RunGc @GcMercuryScope
 *     fun gcMercury()
 *
 *     @RunGc @GcSoftScope @GcMercuryScope
 *     fun gcSoftMercury()
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - Only objects that are **not held** by the application are actually cleared.
 *   If you still hold a strong reference to a cached object, `@RunGc` will not destroy it.
 * - When multiple scope annotations are combined, the final scope is their **intersection** —
 *   only objects matching **all** specified scopes are collected.
 * - Objects cached with `Weak` references are almost always collected when not in use.
 * - Objects cached with `Strong` references are not collected unless the scope
 *   is switched first (see [SwitchCache]).
 *
 * @see GcAllScope
 * @see GcStrongScope
 * @see GcSoftScope
 * @see GcWeakScope
 * @see GcScopeAnnotation
 * @see SwitchCache
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class RunGc 
