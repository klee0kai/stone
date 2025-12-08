package com.github.klee0kai.test.di.bindinstance.singlemethod

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.IPlanet
import com.github.klee0kai.test.mowgli.galaxy.Sun

@Component
interface PlanetComponent {
    fun sunModule(): SunModule?

    @BindInstance
    fun planet(planet: IPlanet?): IPlanet?

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun earth(earth: Earth?): Earth?

    fun providePlanet(): IPlanet?

    @BindInstance
    fun bindSun(sun: Sun?)
}
