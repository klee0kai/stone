package com.github.klee0kai.stone.annotations.wrappers

import kotlin.reflect.KClass

/**
 * Provide custom wrappers creator, class
 *
 *
 * Should implement [Wrapper] or [ProviderWrapper] or [CircleWrapper]
 * and declare custom wrappers in annotation
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class WrappersCreator(
    /**
     * Custom Wrappers, can be provided
     */
    val wrappers: Array<KClass<*>> = []
)
