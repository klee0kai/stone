package com.github.klee0kai.wiki.provide.identifiers

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.SolarSystem
import com.github.klee0kai.test.mowgli.galaxy.Sun
import com.github.klee0kai.test.wire.Wire
import com.github.klee0kai.test.wire.types.MiniUsb
import com.github.klee0kai.test.wire.types.Usb

@Module
abstract class SevenPlanetModule {

    open fun earth(): Earth {
        return Earth()
    }

    abstract fun sun(): Sun

    abstract fun solarSystem(sun: Sun, earth: Earth): SolarSystem

    abstract fun usb_miniusb(): Wire<Usb, MiniUsb>
}
