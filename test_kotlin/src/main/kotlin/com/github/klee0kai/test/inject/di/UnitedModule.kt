package com.github.klee0kai.test.inject.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.inject.forest.Blood
import com.github.klee0kai.test.inject.forest.Earth
import com.github.klee0kai.test.inject.forest.History
import java.awt.Color

@Module
abstract class UnitedModule {
    @Provide(cache = Provide.CacheType.Strong)
    open fun blood(): Blood {
        return Blood(Color.RED)
    }

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun earth(): Earth?

    @Provide(cache = Provide.CacheType.Weak)
    abstract fun history(): History?
}