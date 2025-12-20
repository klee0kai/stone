package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.weakref.Provider
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper
import kotlin.reflect.KClass

@WrappersCreator(wrappers = [CarLazy::class, CarProvide::class])
class CarProviderWrapper : ProviderWrapper {

    override fun <Wr : Any, T : Any> wrap(
        wrapperCl: KClass<Wr>,
        originalProvider: Provider<T?>?,
    ): Wr? {
        return when (wrapperCl) {
            CarLazy::class -> {
                CarLazy { originalProvider?.get() } as Wr
            }

            CarProvide::class -> {
                CarProvide<T> { originalProvider?.get() } as Wr
            }

            else -> null
        }
    }

}
