package com.github.klee0kai.test.di.bindinstance.simple;


import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.galaxy.*;

@Module
public interface SunSystemModule {

    @BindInstance
    Sun sun();

    @BindInstance
    IStar star();

    @BindInstance
    IPlanet planet();

    @BindInstance
    Earth earth();

    @BindInstance
    Saturn saturn();


}
