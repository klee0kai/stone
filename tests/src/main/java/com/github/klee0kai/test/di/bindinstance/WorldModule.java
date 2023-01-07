package com.github.klee0kai.test.di.bindinstance;


import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.world.Earth;
import com.github.klee0kai.test.mowgli.world.IPlanet;
import com.github.klee0kai.test.mowgli.world.Saturn;
import com.github.klee0kai.test.mowgli.world.Sun;

@Module
public interface WorldModule {

    @BindInstance
    Sun sun();

    @BindInstance
    IPlanet planet();

    @BindInstance
    Earth earth();


    @BindInstance
    Saturn saturn();


}
