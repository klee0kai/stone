package com.github.klee0kai.stone

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.types.wrappers.WrapperCreator
import javax.inject.Provider

@WrappersCreator(
    wrappers = [
        Lazy::class
    ]
)
open class KotlinWrappersStone : WrapperCreator {

    override fun <Wr : Any?, T : Any?> provideWrapped(wrapperCl: Class<Wr>, originalProvider: Provider<T>): Wr? {
        return when {
            wrapperCl == Lazy::class.java -> lazy { originalProvider.get() } as Wr
            else -> null
        }
    }

}