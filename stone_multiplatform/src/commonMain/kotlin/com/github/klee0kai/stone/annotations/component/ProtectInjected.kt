package com.github.klee0kai.stone.annotations.component

/**
 * Temporarily protects injected objects from garbage collection.
 *
 * When a dependency consumer (e.g., an Android Activity) is re-created, its previously
 * injected objects may be garbage-collected before the new instance can receive them.
 * `@ProtectInjected` prevents this by temporarily switching the caching strategy
 * to **strong** for the specified duration, ensuring objects survive the transition.
 *
 * ---
 *
 * ## Basic usage
 *
 * ```kotlin
 * @Component
 * interface RainForestComponent {
 *     fun inject(gorilla: Gorilla?)
 *
 *     @RunGc @GcAllScope
 *     fun gcAll()
 *
 *     @ProtectInjected(timeMillis = 50)
 *     fun protectInjected(gorilla: Gorilla)
 * }
 * ```
 *
 * ---
 *
 * ## Protecting all injected objects
 *
 * A parameterless method protects all previously injected objects in the component:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun inject(horse: Horse?)
 *     fun inject(mowgli: Mowgli?)
 *
 *     @ProtectInjected(timeMillis = 30)
 *     fun protectInjected(horse: Horse?)
 *
 *     @ProtectInjected(timeMillis = 30)
 *     fun protectInjected(mowgli: Mowgli?)
 * }
 * ```
 *
 * ---
 *
 * ## With lifecycle owner
 *
 * For lifecycle-aware components (e.g., Android Activities), use [com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner]
 * to automate protection. The lifecycle owner automatically calls `protectForInjected`
 * when appropriate lifecycle events occur:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun inject(horse: Horse?, stoneLifeCycleOwner: StoneLifeCycleOwner?)
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - Protection works by temporarily switching the cache to **strong**, so objects
 *   cannot be garbage-collected during the protection window.
 * - After [timeMillis] elapses, the cache reverts to its original type.
 * - Default protection time is **5 seconds** (5000ms).
 * - The method accepts the target object as a parameter to protect only the objects
 *   that were injected into that specific instance.
 *
 * @see SwitchCache
 * @see RunGc
 * @see com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ProtectInjected(
    /**
     * Duration in milliseconds for which injected objects are protected from GC.
     *
     * After this time, the cache type reverts to its original configuration.
     * Defaults to **5000ms** (5 seconds).
     */
    val timeMillis: Long = 5000L
)
