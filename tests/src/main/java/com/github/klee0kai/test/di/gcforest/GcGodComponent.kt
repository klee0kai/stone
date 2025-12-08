package com.github.klee0kai.test.di.gcforest

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Saturn
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Component
abstract class GcGodComponent : GcEarthComponent() {
    abstract fun sunSystem(): GcSunSystemModule?

    abstract fun earth(): GcEarthModule?

    @BindInstance
    abstract fun bind(sun: Sun?)

    @BindInstance
    abstract fun bind(earth: Earth?)

    @BindInstance
    abstract fun bind(saturn: Saturn?)


    @RunGc
    @GcAllScope
    abstract fun gcAll()

    @RunGc
    @GcStrongScope
    abstract fun gcStrong()

    @RunGc
    @GcSoftScope
    abstract fun gcSoft()

    @RunGc
    @GcWeakScope
    abstract fun gcWeak()

    @RunGc
    @GcSunScope
    abstract fun gcSun()

    @RunGc
    @GcPlanetScope
    abstract fun gcPlanets()

    fun gcSunAndPlanets() {
        gcSun()
        gcPlanets()
    }
}
