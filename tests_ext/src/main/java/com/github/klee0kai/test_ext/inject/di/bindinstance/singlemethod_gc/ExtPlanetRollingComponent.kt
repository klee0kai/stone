package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod_gc

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.StarModule
import com.github.klee0kai.test_ext.inject.di.gcscopes.GcSiriusScope
import com.github.klee0kai.test_ext.inject.di.gcscopes.GcSputnikScope
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.sputniks.Moon

@Component
interface ExtPlanetRollingComponent : PlanetRollingComponent {

    override fun sunModule(): StarModule?

    @ExtendOf
    fun extOf(ext: PlanetRollingComponent?)

    @GcSputnikScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun moonStrong(moon: Moon?): Moon?

    @GcSputnikScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    fun moonSoft(moon: Moon?): Moon?

    @GcSputnikScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun moonWeak(moon: Moon?): Moon?


    @RunGc
    @GcAllScope
    fun gcAllExt()

    @RunGc
    @GcStrongScope
    fun gcStrongExt()

    @RunGc
    @GcSoftScope
    fun gcSoftExt()

    @RunGc
    @GcWeakScope
    fun gcWeakExt()

    @RunGc
    @GcSoftScope
    @GcSunScope
    fun gcSoftSunExt()

    @RunGc
    @GcSoftScope
    @GcPlanetScope
    fun gcSoftPlanetsExt()

    @RunGc
    @GcSoftScope
    @GcSputnikScope
    fun gcSoftSputniksExt()

    @RunGc
    @GcSoftScope
    @GcSiriusScope
    fun gcSoftSiriusExt()
}
