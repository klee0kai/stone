package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Module
public interface GcSunSystemModule {

    @GcSunScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    Sun sun();

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    IPlanet planet();

    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    Earth earth();


    @GcPlanetScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    Saturn saturn();


}
