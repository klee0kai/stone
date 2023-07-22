package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper
import javax.inject.Provider

@WrappersCreator(wrappers = [CarLazy::class, CarProvide::class])
class CarProviderWrapper : ProviderWrapper {
    override fun <Wr, T> wrap(wrapperCl: Class<Wr>, originalProvider: Provider<T>): Wr {
        if (wrapperCl == CarLazy::class.java) {
            return CarLazy { originalProvider.get() } as Wr
        }
        return if (wrapperCl == CarProvide::class.java) {
            CarProvide { originalProvider.get() } as Wr
        } else null as Wr
    }
}
