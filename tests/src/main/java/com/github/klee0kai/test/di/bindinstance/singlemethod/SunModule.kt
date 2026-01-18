package com.github.klee0kai.test.di.bindinstance.singlemethod

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope
import com.github.klee0kai.test.mowgli.galaxy.IStar
import com.github.klee0kai.test.mowgli.galaxy.Sun
import javax.inject.Named


@Module
interface SunModule {
    @GcSunScope
    @Named("strong")
    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun sunStrong(sun: Sun?): Sun?

    @GcSunScope
    @Named("soft")
    @BindInstance(cache = BindInstance.CacheType.Soft)
    fun sunSoft(sun: Sun?): Sun?


    @GcSunScope
    @BindInstance
    fun sun(sun: Sun?): Sun?


    @GcSunScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun star(star: IStar?): IStar?
}
