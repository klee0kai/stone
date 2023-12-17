package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.PhantomProvide
import com.github.klee0kai.test.tech.components.Battery
import com.github.klee0kai.test.tech.components.Ram
import javax.inject.Inject

val DI: TechFactoryComponent = Stone.createComponent(TechFactoryComponent::class.java)

class GoodPhone {

    @Inject
    lateinit var battery: PhantomProvide<Battery>

    @Inject
    lateinit var ram: LazyProvide<Ram>

    fun create() {
        DI.inject(this)
    }

}
