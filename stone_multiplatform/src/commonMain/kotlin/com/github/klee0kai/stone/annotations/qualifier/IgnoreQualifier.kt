package com.github.klee0kai.stone.annotations.qualifier

/**
 * For dependencies, we ignore all qualifier rules.
 * If we collect dependencies into a collection, all available implementations with all qualifiers will be collected.
 *
 * @see [Qualifier]
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

