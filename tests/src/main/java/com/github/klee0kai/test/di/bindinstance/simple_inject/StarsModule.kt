package com.github.klee0kai.test.di.bindinstance.simple_inject

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.mowgli.galaxy.IStar
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Module
interface StarsModule {
    @BindInstance
    fun star(): IStar?

    @BindInstance
    fun sun(): Sun?
}
