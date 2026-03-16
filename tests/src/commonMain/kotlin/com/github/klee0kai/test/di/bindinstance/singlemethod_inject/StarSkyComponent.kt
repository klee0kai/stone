package com.github.klee0kai.test.di.bindinstance.singlemethod_inject

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.stone.annotations.component.RunGc
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.IPlanet
import com.github.klee0kai.test.mowgli.galaxy.Mercury

@Component
interface StarSkyComponent {
    fun starModule(): StarSkyModule?

    @BindInstance
    fun planet(planet: IPlanet?): IPlanet?

    @BindInstance
    fun earth(earth: Earth?): Earth?

    @BindInstance(cache = BindInstance.CacheType.Weak)
    fun mercury(earth: Mercury?): Mercury?

    @RunGc
    @GcAllScope
    fun gcAll()

    fun inject(moonSky: MoonSky?)

    @ProtectInjected(timeMillis = 50)
    fun protectInjected(moonSky: MoonSky?)

}
