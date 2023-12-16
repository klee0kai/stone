package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.Ram

@Module
interface TechFactoryModule {

    @Provide(cache = Provide.CacheType.Factory)
    fun battery(): Battery

    @Provide(cache = Provide.CacheType.Factory)
    fun ram(): Ram

}
