package com.github.klee0kai.test.di.bindinstance.simple_inject;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;

@Module
public interface SevenPlanetModule {


    @BindInstance
    Earth earth();

    @BindInstance
    Mercury mercury();

    @BindInstance
    IPlanet planet();

}
