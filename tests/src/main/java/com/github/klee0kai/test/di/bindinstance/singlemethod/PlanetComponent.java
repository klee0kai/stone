package com.github.klee0kai.test.di.bindinstance.singlemethod;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public interface PlanetComponent {

    SunModule sunModule();

    @BindInstance
    IPlanet planet(IPlanet planet);

    @BindInstance(cache = BindInstance.CacheType.Weak)
    Earth earth(Earth earth);

    IPlanet providePlanet();

    @BindInstance
    void bindSun(Sun sun);


}
