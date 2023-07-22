package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator
import com.github.klee0kai.stone.wrappers.creators.Wrapper

@WrappersCreator(wrappers = [CarRef::class])
class CarWrapper : Wrapper {
    override fun <Wr, T> wrap(wrapperCl: Class<Wr>, original: T): Wr {
        return if (wrapperCl == CarRef::class.java) {
            CarRef(original) as Wr
        } else null as Wr
    }
}
