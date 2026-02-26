package com.github.klee0kai.stone

/**
 * Identifies qualifier annotations. Anyone can define a new qualifier. A
 * qualifier annotation:
 *
 * - is annotated with `@Qualifier`, `@Retention(RUNTIME)`,
 *      and typically `@Documented`.
 * - can have attributes.
 * - may be part of the public API, much like the dependency type, but
 *      unlike implementation types which needn't be part of the public
 *      API.
 * - may have restricted usage if annotated with {@code @Target}. While
 *      this specification covers applying qualifiers to fields and
 *      parameters only, some injector configurations might use qualifier
 *      annotations in other places (on methods or classes for example).
 *
 *
 * For example:
 *
 * ```
 *   @Documented
 *   @Retention(RUNTIME)
 *   @Qualifier
 *   annotation class Leather(
 *     val color: Color
 *   )
 * ```
 *
 * @see [Named]
 */
annotation class Qualifier