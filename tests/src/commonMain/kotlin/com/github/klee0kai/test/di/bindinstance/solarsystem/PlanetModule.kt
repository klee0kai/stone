package com.github.klee0kai.test.di.bindinstance.solarsystem

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.mowgli.galaxy.*

@Module
open class PlanetModule {
    @Provide(cache = Provide.CacheType.Strong)
    open fun sun(solarSystem: SolarSystem?): Sun? {
        return Sun()
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun earth(sun: Sun?): Earth? {
        return Earth()
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun mercury(sun: Sun?): Mercury? {
        return Mercury()
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun saturn(sun: Sun?): Saturn? {
        return Saturn()
    }
}
