package com.github.klee0kai.stone.annotations.component

import com.github.klee0kai.stone.Scope


/**
 * A built-in scope that targets objects cached with **weak** references
 * (`@Provide(cache = CacheType.Weak)`).
 *
 * Used with [RunGc] or [SwitchCache] to selectively manage weak-cached objects:
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent {
 *     @RunGc @GcWeakScope
 *     abstract fun gcWeak()
 * }
 * ```
 *
 * Weak-cached objects are almost always collected when not held by anyone,
 * so `@RunGc @GcWeakScope` is typically the least impactful GC operation.
 *
 * @see RunGc
 * @see SwitchCache
 * @see GcScopeAnnotation
 */
@GcScopeAnnotation
@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcWeakScope 
