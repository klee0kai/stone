package com.github.klee0kai.test.di.bindinstance.singlemethod_inject

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope
import com.github.klee0kai.test.mowgli.galaxy.IStar
import com.github.klee0kai.test.mowgli.galaxy.Sun


@Module
interface StarSkyModule {

    @GcSunScope
    @BindInstance
    fun sun(sun: Sun?): Sun?


    @GcSunScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun star(star: IStar?): IStar?
}
