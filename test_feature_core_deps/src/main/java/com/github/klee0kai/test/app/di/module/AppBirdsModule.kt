package com.github.klee0kai.test.app.di.module

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.app.birds.Pelican
import com.github.klee0kai.test.core.di.modules.BirdsModule

@Module
interface AppBirdsModule : BirdsModule {

    @Provide(cache = Provide.CacheType.Weak)
    fun pelican(): Pelican

}