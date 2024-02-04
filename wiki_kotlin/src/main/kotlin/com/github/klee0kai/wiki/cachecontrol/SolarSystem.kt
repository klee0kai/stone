package com.github.klee0kai.wiki.cachecontrol

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import javax.inject.Inject

val DI: PlanetsComponent = Stone.createComponent(PlanetsComponent::class.java)

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
