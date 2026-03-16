package com.github.klee0kai.test.di.house.simple

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.house.kitchen.storagearea.Clothes
import com.github.klee0kai.test.house.kitchen.storagearea.Cookware
import com.github.klee0kai.test.house.kitchen.storagearea.Sanitizers

@Module
interface ToolsModule {
    @Provide(cache = Provide.CacheType.Factory)
    fun cookware(): Cookware?

    @Provide(cache = Provide.CacheType.Factory)
    fun clothes(): Clothes?

    @Provide(cache = Provide.CacheType.Factory)
    fun sanitizers(): Sanitizers?
}
