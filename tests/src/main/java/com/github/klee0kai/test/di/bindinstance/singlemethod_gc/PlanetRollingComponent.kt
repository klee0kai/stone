package com.github.klee0kai.test.di.bindinstance.singlemethod_gc

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.di.bindinstance.singlemethod.SunModule
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.IPlanet

@Component
interface PlanetRollingComponent {
    fun sunModule(): SunModule?

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    fun earthStrong(earth: Earth?): Earth?

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    fun earthSoft(earth: Earth?): Earth?

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun planet(planet: IPlanet?): IPlanet?

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun earth(earth: Earth?): Earth?

    fun providePlanet(): IPlanet?

    @RunGc
    @GcAllScope
    fun gcAll()

    @RunGc
    @GcStrongScope
    fun gcStrong()

    @RunGc
    @GcSoftScope
    fun gcSoft()

    @RunGc
    @GcWeakScope
    fun gcWeak()


    @RunGc
    @GcSoftScope
    @GcSunScope
    fun gcSoftSun()

    @RunGc
    @GcSoftScope
    @GcPlanetScope
    fun gcSoftPlanets()
}
