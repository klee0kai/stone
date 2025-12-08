package com.github.klee0kai.test.di.bindinstance.simple

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.mowgli.galaxy.*


@Module
interface SunSystemModule {
    @BindInstance
    fun sun(): Sun?

    @BindInstance
    fun star(): IStar?

    @BindInstance
    fun planet(): IPlanet?

    @BindInstance
    fun earth(): Earth?

    @BindInstance
    fun saturn(): Saturn?
}
