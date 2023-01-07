package com.github.klee0kai.test.di.bindinstance;


import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Module
public interface SunSystemModule {

    @BindInstance
    Sun sun();

    @BindInstance
    IPlanet planet();

    @BindInstance
    Earth earth();


    @BindInstance
    Saturn saturn();


}
