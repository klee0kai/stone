package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.di.bindinstance.singlemethod.SunModule
import com.github.klee0kai.test_ext.inject.di.gcscopes.GcSiriusScope
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.stars.Sirius

@Module
interface StarModule : SunModule {

    @GcSiriusScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun siriusStrong(sirius: Sirius?): Sirius?

    @GcSiriusScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    fun siriusSoft(sirius: Sirius?): Sirius?

    @GcSiriusScope
    @BindInstance
    fun sirius(sirius: Sirius?): Sirius?

    @GcSiriusScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun siriusWeak(star: Sirius?): Sirius?

}
