package com.github.klee0kai.test_kotlin.di.compfactory

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.KConnectType
import com.github.klee0kai.test_kotlin.tech.components.Keyboard
import com.github.klee0kai.test_kotlin.tech.components.Monitor

@Module
abstract class CompFactoryModule {

    @Provide(cache = Provide.CacheType.Factory)
    abstract fun monitor(): Monitor

    @Provide(cache = Provide.CacheType.Factory)
    abstract fun keyboard(kConnectType: KConnectType): Keyboard


}