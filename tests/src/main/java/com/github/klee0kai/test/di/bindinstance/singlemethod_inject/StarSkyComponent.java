package com.github.klee0kai.test.di.bindinstance.singlemethod_inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;

@Component
public interface StarSkyComponent {

    StarSkyModule startModule();

    @BindInstance
    IPlanet planet(IPlanet planet);

    @BindInstance
    Earth earth(Earth earth);

    @BindInstance
    Mercury mercury(Mercury earth);

    void inject(MoonSky moonSky);
}
