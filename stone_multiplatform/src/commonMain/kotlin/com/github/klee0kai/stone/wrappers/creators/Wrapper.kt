package com.github.klee0kai.stone.wrappers.creators

import kotlin.reflect.KClass

interface Wrapper {
    /**
     * Provide wrapped object.
     *
     * @param wrapperCl type of wrapper
     * @param original  original object
     * @param <Wr>      type of wrapper
     * @param <T>       type of providing original object
     * @return wrapped object provider
    </T></Wr> */
    fun <Wr : Any, T : Any> wrap(wrapperCl: KClass<Wr>?, original: T?): Wr?
}
