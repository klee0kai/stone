package com.github.klee0kai.stone.annotations.component

/**
 * A built-in scope that targets **all** cached objects in the component,
 * regardless of their cache type or custom scope.
 *
 * Used with [RunGc] to collect all unreferenced objects,
 * or with [SwitchCache] to change the caching strategy globally:
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent {
 *     abstract fun sunSystem(): GcSunSystemModule?
 *
 *     @RunGc @GcAllScope
 *     abstract fun gcAll()
 *
 *     @GcAllScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Weak)
 *     abstract fun allWeak()
 * }
 * ```
 *
 * @see RunGc
 * @see SwitchCache
 * @see GcScopeAnnotation
 */
@GcScopeAnnotation
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcAllScope 
