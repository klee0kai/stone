package com.github.klee0kai.stone.annotations.qualifier


/**
 * TODO kdoc
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

