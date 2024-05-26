package com.github.klee0kai.test.core.di.modules

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.core.birds.*

@Module
interface BirdsModule {

    @Provide(cache = Provide.CacheType.Weak)
    fun crow(): Crow = Crow()

    @Provide(cache = Provide.CacheType.Soft)
    fun duck(): Duck = Duck()

    @Provide(cache = Provide.CacheType.Strong)
    fun hen(): Hen = Hen()

    @Provide(cache = Provide.CacheType.Weak)
    fun pigeon(): Pigeon

    @Provide(cache = Provide.CacheType.Weak)
    fun sparrow(): Sparrow = SparrowImpl()

    @Provide(cache = Provide.CacheType.Weak)
    fun turkey(): Turkey = Turkey()

}