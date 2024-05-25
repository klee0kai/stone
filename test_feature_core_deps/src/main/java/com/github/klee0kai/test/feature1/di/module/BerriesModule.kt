package com.github.klee0kai.test.feature1.di.module

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.feature1.berries.*

@Module
interface BerriesModule {

    @Provide(cache = Provide.CacheType.Weak)
    fun berries(): Berries = Berries()

    @Provide(cache = Provide.CacheType.Weak)
    fun cherry(): Cherry = Cherry()

    @Provide(cache = Provide.CacheType.Weak)
    fun currant(): Currant = Currant()

    @Provide(cache = Provide.CacheType.Weak)
    fun raspberry(): Raspberry = Raspberry()

    @Provide(cache = Provide.CacheType.Weak)
    fun strawberry(): Strawberry = Strawberry()

}