package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.wrappers.creators.ProviderWrapper
import javax.inject.Provider

@WrappersCreator(wrappers = [CustomLazy::class])
class CustomWrapper : ProviderWrapper {

    override fun <Wr, T> wrap(wrapperCl: Class<Wr>, originalProvider: Provider<T>): Wr? {
        if (wrapperCl == CustomLazy::class.java) {
            return CustomLazy { originalProvider.get() } as Wr
        }
        return null
    }

}
