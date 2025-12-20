package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.weakref.Provider
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper
import kotlin.reflect.KClass

@WrappersCreator(
    wrappers = [
        Lazy::class,
    ]
)
open class KotlinWrappersStone : ProviderWrapper {

    override fun <Wr : Any, T : Any> wrap(
        wrapperCl: KClass<Wr>,
        originalProvider: Provider<T?>?
    ): Wr? {
        return when {
            wrapperCl == Lazy::class -> lazy { originalProvider?.get() } as Wr
            else -> null
        }
    }

}