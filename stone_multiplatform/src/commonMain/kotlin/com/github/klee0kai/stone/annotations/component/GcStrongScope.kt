package com.github.klee0kai.stone.annotations.component

import com.github.klee0kai.stone.Scope


/**
 * A built-in scope that targets objects cached with **strong** references
 * (`@Provide(cache = CacheType.Strong)`).
 *
 * Used with [RunGc] or [SwitchCache] to selectively manage strongly-cached objects:
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent {
 *     @RunGc @GcStrongScope
 *     abstract fun gcStrong()
 *
 *     @GcStrongScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Weak)
 *     abstract fun strongToWeak()
 * }
 * ```
 *
 * Since strong references are never reclaimed automatically, `@RunGc @GcStrongScope`
 * is the only way to release them (or switch them to a weaker cache type first).
 *
 * @see RunGc
 * @see SwitchCache
 * @see GcScopeAnnotation
 */
@GcScopeAnnotation
@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcStrongScope 
