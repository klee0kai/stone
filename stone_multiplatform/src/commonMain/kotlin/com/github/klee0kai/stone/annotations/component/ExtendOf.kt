package com.github.klee0kai.stone.annotations.component


/**
 * Declares a component extension method that inherits DI state from a parent component.
 *
 * When a component extends another, it inherits the parent's cached objects and modules.
 * The child component can override module methods to provide new implementations
 * while sharing the same DI graph.
 *
 * ---
 *
 * ## Basic extension
 *
 * ```kotlin
 * @Component
 * interface AppComponent {
 *     fun feature(): FeatureModule
 *
 *     @Init
 *     fun initFeatureModule(featureModule: FeatureModule?)
 * }
 *
 * @Component
 * interface AppProComponent : AppComponent {
 *     override fun feature(): ProFeatureModule
 *
 *     @ExtendOf
 *     fun extendComponent(parent: AppComponent)
 * }
 * ```
 *
 * Usage:
 *
 * ```kotlin
 * val baseComponent = AppComponentStoneComponent()
 * val proComponent = AppProComponentStoneComponent()
 * proComponent.extendComponent(baseComponent)
 * ```
 *
 * ---
 *
 * ## Nuances
 *
 * - After extension, the parent component's factories are replaced with the child's.
 *   Both components become **interconnected** — all cleanups and caching type
 *   changes are performed simultaneously for both.
 * - The replacement of provided objects is **not immediate but gradual** —
 *   old cached instances are replaced as they are cleared from memory.
 * - New objects provided by the child should **extend** the functionality
 *   of the previous ones, not break existing logic. During the transition period,
 *   interaction between objects of different versions is possible.
 * - The extending component must inherit (implement) the parent component's interface.
 *
 * @see Component
 * @see Init
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ExtendOf 
