package com.github.klee0kai.test.di.bindinstance.singlemethod_gc;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.di.bindinstance.singlemethod.SunModule;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;

@Component
public interface PlanetRollingComponent {

    SunModule sunModule();

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    Earth earthStrong(Earth earth);

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    Earth earthSoft(Earth earth);

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    IPlanet planet(IPlanet planet);

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    Earth earth(Earth earth);

    IPlanet providePlanet();

    @RunGc
    @GcAllScope
    void gcAll();

    @RunGc
    @GcStrongScope
    void gcStrong();

    @RunGc
    @GcSoftScope
    void gcSoft();

    @RunGc
    @GcWeakScope
    void gcWeak();


    @RunGc
    @GcSoftScope
    @GcSunScope
    void gcSoftSun();

    @RunGc
    @GcSoftScope
    @GcPlanetScope
    void gcSoftPlanets();


}
