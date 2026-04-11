package com.github.klee0kai.stone.annotations.component

import com.github.klee0kai.stone.Scope


/**
 * A built-in scope that targets objects cached with **soft** references
 * (`@Provide(cache = CacheType.Soft)`).
 *
 * Used with [RunGc] or [SwitchCache] to selectively manage soft-cached objects:
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent {
 *     @RunGc @GcSoftScope
 *     abstract fun gcSoft()
 *
 *     @GcSoftScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
 *     abstract fun protectSoftTemporarily()
 * }
 * ```
 *
 * @see RunGc
 * @see SwitchCache
 * @see GcScopeAnnotation
 */
@GcScopeAnnotation
@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcSoftScope 
