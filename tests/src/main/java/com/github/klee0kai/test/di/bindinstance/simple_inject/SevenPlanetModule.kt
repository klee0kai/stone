package com.github.klee0kai.test.di.bindinstance.simple_inject

import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.IPlanet
import com.github.klee0kai.test.mowgli.galaxy.Mercury

@Module
interface SevenPlanetModule {
    @BindInstance
    fun earth(): Earth?

    @BindInstance
    fun mercury(): Mercury?

    @BindInstance
    fun planet(): IPlanet?
}
