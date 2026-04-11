package com.github.klee0kai.stone.annotations.dependencies


/**
 * Marks an interface as an external dependency contract for a component.
 *
 * A `@Dependencies` interface declares objects that come from outside the component
 * (e.g., from another component or a factory). Unlike modules, dependencies are
 * **not cached** by the consuming component — they are already cached in their source.
 * Dependencies **must be initialized** (via `@Init`) before use.
 *
 * ---
 *
 * ## Declaring dependencies
 *
 * ```kotlin
 * @Dependencies
 * interface StarsDependencies {
 *     fun sun(): Sun
 * }
 *
 * @Dependencies
 * interface PlanningDependencies {
 *     fun workCalendar(): WorkCalendar?
 *     fun securityDepartment(): SecurityDepartment?
 * }
 * ```
 *
 * ---
 *
 * ## Using dependencies in a component
 *
 * Declare the dependency interface as a method in the component and initialize it
 * with `@Init`:
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
 * ## Providing dependencies from another component
 *
 * Any component, factory, or class can provide dependencies by implementing the interface:
 *
 * ```kotlin
 * @Component
 * abstract class AppComponent : CommonDependencies {
 *     // provides CommonDependencies to child components
 * }
 * ```
 *
 * ---
 *
 * ## Dependency composition (inheritance)
 *
 * Dependencies can extend other dependency interfaces to compose contracts:
 *
 * ```kotlin
 * @Dependencies
 * interface CoreDependenciesProvider : BirdsDependencies, TreesDependencies {
 *     fun alder(): Alder
 *     fun ash(): Ash
 *     fun beech(): Beech
 * }
 * ```
 *
 * ---
 *
 * ## Dependency methods with wrappers
 *
 * Dependency methods can return wrapper types:
 *
 * ```kotlin
 * @Dependencies
 * interface BerriesDependencies {
 *     fun currant(): AsyncLazy<Currant>
 *     fun raspberry(): AsyncLazy<Raspberry>
 *     fun strawberry(): AsyncLazy<Strawberry>
 * }
 *
 * interface SolarSystemDependencies {
 *     fun earth(): LazyProvider<Earth?>?
 *     fun mercury(): LazyProvider<Mercury?>?
 * }
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - Dependencies **must be initialized** before use — accessing an uninitialized
 *   dependency results in `null`.
 * - Dependencies are **not cached** by the consuming component — they delegate
 *   to the source that provides them.
 * - The dependency interface signature is similar to a module's, but without
 *   `@Provide` or `@BindInstance` annotations.
 *
 * @see com.github.klee0kai.stone.annotations.component.Component
 * @see com.github.klee0kai.stone.annotations.component.Init
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Dependencies 
