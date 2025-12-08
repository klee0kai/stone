package com.github.klee0kai.test.di.bindinstance.simple;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public interface GodWorkspaceComponent {

    SunSystemModule sunSystem();

    @BindInstance
    void bindPlanet(IPlanet planet);

    @BindInstance
    IPlanet planet(IPlanet planet);

    @BindInstance
    void bindSun(Sun sun);

    @BindInstance
    void bindEarth(Earth earth);

    @BindInstance
    void bindSaturn(Saturn saturn);


    IPlanet providePlanet();

}
