package com.github.klee0kai.wiki.cachecontrol

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Component
interface PlanetsComponent {

    fun sunModule(): PlanetsModule?

    @RunGc
    @GcMercuryScope
    fun gcMercury()

    @RunGc
    @GcSoftScope
    @GcMercuryScope
    fun gcSoftMercury()


    @SunScope
    @BindInstance
    fun sun(sun: Sun?): Sun?

    @SunScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    fun protectSun()


    fun inject(solarSystem: SolarSystem?)

    @ProtectInjected(timeMillis = 30)
    fun protectInjected(solarSystem: SolarSystem?)

    fun inject(solarSystem: SolarSystem?, lifeCycleOwner: StoneLifeCycleOwner?)
}