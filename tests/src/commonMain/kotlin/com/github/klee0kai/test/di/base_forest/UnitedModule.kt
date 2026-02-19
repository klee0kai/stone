package com.github.klee0kai.test.di.base_forest

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.body.Blood
import com.github.klee0kai.test.mowgli.community.History
import com.github.klee0kai.test.mowgli.galaxy.Earth

@Module
abstract class UnitedModule {

    @Provide(cache = Provide.CacheType.Strong)
    open fun blood(): Blood? {
        return Blood(color = 2)
    }

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun earth(): Earth?

    @Provide(cache = Provide.CacheType.Weak)
    abstract fun history(): History?
}
