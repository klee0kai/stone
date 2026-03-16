package com.github.klee0kai.wiki.cachecontrol

import com.github.klee0kai.test.mowgli.galaxy.Mercury
import javax.inject.Inject

val DI: PlanetsComponent = PlanetsComponentStoneComponent()

class SolarSystem {

    @Inject
    var mercury: Mercury? = null

    fun create() {
        DI.inject(this)
    }

    fun beforeRecreate() {
        DI.protectInjected(this)
    }

}
