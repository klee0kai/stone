package com.github.klee0kai.test.core.di.wrapper

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.wrappers.creators.CircleWrapper
import javax.inject.Provider

@WrappersCreator(
    wrappers = [
        CustomStoneProvide::class
    ]
)
class CustomWrappersStone : CircleWrapper {

    override fun <Wr : Any?, T : Any?> wrap(wrapperCl: Class<Wr>?, originalProvider: Provider<T>?): Wr? {
        return when {
            wrapperCl == CustomStoneProvide::class.java -> CustomStoneProvide { originalProvider?.get() } as Wr
            else -> null
        }
    }

    override fun <Wr : Any?, T : Any?> unwrap(wrapperCl: Class<Wr>?, objectType: Class<T>?, wrapper: Wr): T? {
        return when {
            wrapperCl == CustomStoneProvide::class.java -> (wrapper as CustomStoneProvide<T>).get()
            else -> null
        }
    }
}