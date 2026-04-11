package com.github.klee0kai.stone.annotations.component


/**
 * Marks a method in a component for initializing modules or dependencies.
 *
 * An `@Init` method accepts one or more module/dependency instances as parameters
 * and replaces the currently active instances in the component. This allows
 * runtime replacement of modules (e.g., swapping a test module for a production one)
 * and initialization of external dependencies.
 *
 * ---
 *
 * ## Single module initialization
 *
 * ```kotlin
 * @Component
 * interface AppComponent {
 *     fun feature(): FeatureModule
 *     fun starsDependencies(): StarsDependencies
 *
 *     @Init
 *     fun initFeatureModule(featureModule: FeatureModule?)
 * }
 * ```
 *
 * ---
 *
 * ## Multiple modules in a single method
 *
 * A single `@Init` method can accept multiple module parameters:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun united(): UnitedModule?
 *     fun identity(): IdentityModule?
 *
 *     @Init
 *     fun initUnitedModule(unitedModule: UnitedModule?)
 *
 *     @Init
 *     fun initAllModules(unitedModule: UnitedModule?, identityModule: IdentityModule?)
 * }
 * ```
 *
 * ---
 *
 * ## Dependency initialization
 *
 * Dependencies (classes annotated with `@Dependencies`) **must be initialized** before use,
 * as they are not created by Stone — they come from external components:
 *
 * ```kotlin
 * @Component
 * interface FeatureComponent {
 *     fun dependencies(): CommonDependencies
 *
 *     @Init
 *     fun initDependencies(dependencies: CommonDependencies?)
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - Passing `null` for a parameter leaves the corresponding module/dependency unchanged.
 * - After initialization, provided objects are replaced **gradually** — old cached
 *   instances remain until they are cleared from memory by GC or explicit `@RunGc`.
 * - Initialization can be performed at component creation time or at any later point.
 * - Multiple `@Init` methods can coexist in a single component.
 * - Dependencies can only be initialized with an instance (not a class reference).
 *
 * @see Component
 * @see ExtendOf
 * @see com.github.klee0kai.stone.annotations.dependencies.Dependencies
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Init 
