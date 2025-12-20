package com.github.klee0kai.stone.wrappers.creators

import com.github.klee0kai.stone.weakref.Provider
import kotlin.reflect.KClass

interface ProviderWrapper {
    /**
     * Provide wrapped object.
     *
     * @param wrapperCl        type of wrapper
     * @param originalProvider original object provider
     * @param <Wr>             type of wrapper
     * @param <T>              type of providing original object
     * @return wrapped object provider
    </T></Wr> */
    fun <Wr : Any, T : Any> wrap(wrapperCl: KClass<Wr>, originalProvider: Provider<T?>?): Wr?
}
