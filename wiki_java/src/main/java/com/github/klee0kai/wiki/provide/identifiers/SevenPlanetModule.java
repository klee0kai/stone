package com.github.klee0kai.wiki.provide.identifiers;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.SolarSystem;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import com.github.klee0kai.test.wire.Wire;
import com.github.klee0kai.test.wire.types.MiniUsb;
import com.github.klee0kai.test.wire.types.Usb;

@Module
public abstract class SevenPlanetModule {

    public Earth earth() {
        return new Earth();
    }

    public abstract Sun sun();

    public abstract SolarSystem solarSystem(Sun sun, Earth earth);

    public abstract Wire<Usb, MiniUsb> usb_miniusb();

}
