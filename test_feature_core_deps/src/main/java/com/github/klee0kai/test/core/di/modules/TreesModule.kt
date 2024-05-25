package com.github.klee0kai.test.core.di.modules

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.core.trees.Fir
import com.github.klee0kai.test.core.trees.Palm
import com.github.klee0kai.test.core.trees.Poplar

@Module
interface TreesModule {

    @Provide(cache = Provide.CacheType.Weak)
    fun fir(): Fir = Fir()

    @Provide(cache = Provide.CacheType.Weak)
    fun palm(): Palm = Palm()

    @Provide(cache = Provide.CacheType.Weak)
    fun poplar(): Poplar = Poplar()

}