package com.github.klee0kai.stone.annotations.qualifier

/**
 * Ignores all qualifier filtering when resolving a dependency.
 *
 * By default, Stone matches dependencies by their qualifier annotations —
 * only providers with a matching qualifier are considered. When `@IgnoreQualifier`
 * is applied, **all** available implementations of the type are collected
 * regardless of their qualifiers. This is especially useful when collecting
 * dependencies into a `List` or `Collection`.
 *
 * ---
 *
 * ## On a component method
 *
 * Collect all instances of a type, ignoring their qualifiers:
 *
 * ```kotlin
 * @Component
 * interface CarQComponent {
 *     fun module1(): CarQPModule?
 *     fun module2(): CarQCModule?
 *
 *     @Named("a")
 *     fun carNameA(): Car?
 *
 *     @MyQualifier
 *     fun carMyQualifier(): Car?
 *
 *     // collects ALL Car instances from all modules, ignoring qualifiers
 *     @IgnoreQualifier
 *     fun allCars(): List<Car?>?
 * }
 * ```
 *
 * ---
 *
 * ## On a method parameter
 *
 * Collect all implementations for a specific parameter:
 *
 * ```kotlin
 * @Module
 * open class CarQCModule {
 *     @Named("blueCar")
 *     @Provide(cache = Provide.CacheType.Factory)
 *     open fun blueCar(
 *         bumpers: List<Bumper>,
 *         wheels: List<Wheel>,
 *         @IgnoreQualifier windows: List<Window>,  // collects ALL windows
 *     ): Car? { ... }
 * }
 * ```
 *
 * @see com.github.klee0kai.stone.Qualifier
 * @see com.github.klee0kai.stone.Named
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
)
@MustBeDocumented
annotation class IgnoreQualifier

