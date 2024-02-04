package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.Ram

@Component(wrapperProviders = [CustomWrapper::class])
interface TechFactoryComponent {

    fun factory(): TechFactoryModule

    fun battery(): PhantomProvide<Battery>

    fun ramMemory(): LazyProvide<Ram>

    fun inject(goodPhone: GoodPhone)
}
