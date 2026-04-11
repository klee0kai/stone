package com.github.klee0kai.stone.annotations.wrappers

/**
 * Marks an object as a custom wrapper type transformer for Stone DI.
 *
 * By default, Stone supports `Provider`, `LazyProvider`, `AsyncLazy`, `AsyncProvider`,
 * `WeakReference`, `WeakRef`, and `SoftRef` as return-type wrappers. To add support
 * for a custom wrapper type, create an object annotated with `@WrappersHelper` and
 * define `transformTo*` methods that accept a `Provider<T>` and return your custom type.
 *
 * The helper must be registered in the component via
 * [Component.wrapperHelpers][com.github.klee0kai.stone.annotations.component.Component.wrapperHelpers].
 *
 * ---
 *
 * ## Declaring a wrapper helper
 *
 * ```kotlin
 * @WrappersHelper
 * object CustomLazyWrapper {
 *     fun <T> transformToCarLay(
 *         origin: Provider<T>,
 *     ): CustomLazy<T> = CustomLazy { origin.get() }
 * }
 * ```
 *
 * ---
 *
 * ## Registering in a component
 *
 * ```kotlin
 * @Component(wrapperHelpers = [CustomLazyWrapper::class])
 * interface TechFactoryComponent {
 *     fun factory(): TechFactoryModule
 *     fun battery(): Provider<Battery>
 *     fun ramMemory(): LazyProvider<Ram>
 *     fun inject(goodPhone: GoodPhone)
 * }
 * ```
 *
 * Multiple helpers can be registered:
 *
 * ```kotlin
 * @Component(wrapperHelpers = [CarBoxedWrapper::class, CarRefWrapper::class])
 * interface CarCustomWrappersComponent {
 *     fun car(): Car?
 *     fun carRef(): CarRef<Car?>?
 *     fun carLazy(): CarLazy<Car?>?
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - Each `transformTo*` method must accept exactly one `Provider<T>` parameter.
 * - The method name prefix `transformTo` is required — the suffix is used
 *   to identify the wrapper type.
 * - The helper must be an `object` (Kotlin singleton), not a class.
 *
 * @see com.github.klee0kai.stone.annotations.component.Component
 * @see com.github.klee0kai.stone.Provider
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class WrappersHelper
