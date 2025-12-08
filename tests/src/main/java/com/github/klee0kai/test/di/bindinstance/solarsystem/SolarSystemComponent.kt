package com.github.klee0kai.test.di.bindinstance.solarsystem

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.mowgli.galaxy.SolarSystem

@Component
interface SolarSystemComponent : SolarSystemDependencies {
    fun planets(): PlanetModule?


    @BindInstance
    fun bind(system: SolarSystem?): SolarSystem?
}
