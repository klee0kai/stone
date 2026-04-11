package com.github.klee0kai.stone.annotations.component

/**
 * Provides access to a module's **original factory** (the user-defined module class),
 * bypassing the Stone-generated wrapper that manages caching.
 *
 * This is useful when you need direct access to the module's creation logic
 * without the caching layer — for example, when you want to call provider
 * methods and always get a fresh instance regardless of the cache type.
 *
 * ---
 *
 * ## Usage
 *
 * ```kotlin
 * @Component(identifiers = [StoreAreaType::class])
 * interface HouseComponent {
 *     fun module(): HouseModule?
 *
 *     @ModuleOriginFactory
 *     fun moduleFactory(): HouseModule?
 * }
 * ```
 *
 * Here `module()` returns the Stone-managed module (with caching),
 * while `moduleFactory()` returns the original module instance (without caching wrappers).
 *
 * ---
 *
 * ## Nuances
 *
 * - The returned module is the raw factory, so calling its methods will
 *   **not** use the component's caching or scope management.
 * - Primarily useful for testing or when you need to inspect the module's
 *   original behavior.
 *
 * @see Component
 * @see com.github.klee0kai.stone.annotations.module.Module
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ModuleOriginFactory 
