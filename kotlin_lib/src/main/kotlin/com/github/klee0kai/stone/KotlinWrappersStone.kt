package com.github.klee0kai.stone

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.types.wrappers.Wrapper
import javax.inject.Provider

@WrappersCreator(
    wrappers = [
        Lazy::class
    ]
)
open class KotlinWrappersStone : Wrapper {

    override fun <Wr : Any?, T : Any?> wrap(wrapperCl: Class<Wr>, originalProvider: Provider<T>): Wr? {
        return when {
            wrapperCl == Lazy::class.java -> lazy { originalProvider.get() } as Wr
            else -> null
        }
    }

    override fun <Wr : Any?, T : Any?> unwrap(wrapped: Wr): T {
        TODO("Not yet implemented")
    }

}