package com.github.klee0kai.test.di.bindinstance.simple_inject

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.IPlanet
import com.github.klee0kai.test.mowgli.galaxy.Mercury

@Component
interface SevenPlanetComponent {
    fun planets(): SevenPlanetModule?

    fun stars(): StarsModule?

    @BindInstance
    fun bind(earth: Earth?)

    @BindInstance
    fun bind(mercury: Mercury?)

    @BindInstance
    fun bindPlanet(planet: IPlanet?)

    fun inject(moonSky: MoonSky?)
}
