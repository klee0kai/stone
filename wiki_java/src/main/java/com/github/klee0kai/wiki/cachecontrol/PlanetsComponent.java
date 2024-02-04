package com.github.klee0kai.wiki.cachecontrol;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public interface PlanetsComponent {

    PlanetsModule sunModule();

    @RunGc
    @GcMercuryScope
    void gcMercury();

    @RunGc
    @GcSoftScope
    @GcMercuryScope
    void gcSoftMercury();


    @SunScope
    @BindInstance
    Sun sun(Sun sun);

    @SunScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    void protectSun();


    void inject(SolarSystem solarSystem);

    @ProtectInjected(timeMillis = 30)
    void protectInjected(SolarSystem solarSystem);

    void inject(SolarSystem solarSystem, StoneLifeCycleOwner lifeCycleOwner);

}