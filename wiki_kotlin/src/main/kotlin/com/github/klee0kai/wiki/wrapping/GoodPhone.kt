package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.Provider
import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.Ram
import javax.inject.Inject

val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()

class GoodPhone {

    @Inject
    lateinit var battery: Provider<Battery>

    @Inject
    lateinit var ram: LazyProvider<Ram>

    fun create() {
        DI.inject(this)
    }

}
