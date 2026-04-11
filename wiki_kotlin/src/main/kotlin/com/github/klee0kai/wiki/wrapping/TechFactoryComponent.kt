package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.Ram

@Component(
    wrapperHelpers = [CustomLazyWrapper::class],
)
interface TechFactoryComponent {

    fun factory(): TechFactoryModule

    fun battery(): Provider<Battery>

    fun ramMemory(): LazyProvider<Ram>

    fun inject(goodPhone: GoodPhone)

}
