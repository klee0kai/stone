package com.github.klee0kai.test.di.bindinstance.solarsystem;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.galaxy.*;

@Module
public class PlanetModule {

    @Provide(cache = Provide.CacheType.Strong)
    Sun sun(SolarSystem solarSystem) {
        return new Sun();
    }

    @Provide(cache = Provide.CacheType.Soft)
    Earth earth(Sun sun){
        return new Earth();
    }

    @Provide(cache = Provide.CacheType.Soft)
    Mercury mercury(Sun sun){
        return new Mercury();
    }

    @Provide(cache = Provide.CacheType.Soft)
    Saturn saturn(Sun sun){
        return new Saturn();
    }


}
