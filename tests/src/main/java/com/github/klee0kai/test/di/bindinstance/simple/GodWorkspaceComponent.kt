package com.github.klee0kai.test.di.bindinstance.simple

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.IPlanet
import com.github.klee0kai.test.mowgli.galaxy.Saturn
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Component
interface GodWorkspaceComponent {

    fun sunSystem(): SunSystemModule?

    @BindInstance
    fun bindPlanet(planet: IPlanet?)

    @BindInstance
    fun planet(planet: IPlanet?): IPlanet?

    @BindInstance
    fun bindSun(sun: Sun?)

    @BindInstance
    fun bindEarth(earth: Earth?)

    @BindInstance
    fun bindSaturn(saturn: Saturn?)

    fun providePlanet(): IPlanet?

}
