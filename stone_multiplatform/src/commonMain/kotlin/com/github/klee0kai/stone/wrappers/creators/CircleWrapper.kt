package com.github.klee0kai.stone.wrappers.creators

import kotlin.reflect.KClass

interface CircleWrapper : ProviderWrapper {
    /**
     * UnProvide wrapped object.
     *
     * @param wrapperCl  type of wrapper
     * @param objectType original provide object type
     * @param wrapper    wrapper to unwrap
     * @param <Wr>       type of wrapper
     * @param <T>        type of providing original object
     * @return wrapped object provider
    </T></Wr> */
    fun <Wr : Any, T : Any> unwrap(wrapperCl: KClass<Wr>?, objectType: KClass<T>?, wrapper: Wr?): T?
}
