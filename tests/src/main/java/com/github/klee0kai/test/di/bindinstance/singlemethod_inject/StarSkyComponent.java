package com.github.klee0kai.test.di.bindinstance.singlemethod_inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.annotations.component.RunGc;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;

@Component
public interface StarSkyComponent {

    StarSkyModule starModule();

    @BindInstance
    IPlanet planet(IPlanet planet);

    @BindInstance
    Earth earth(Earth earth);

    @BindInstance(cache = BindInstance.CacheType.Weak)
    Mercury mercury(Mercury earth);

    @RunGc
    @GcAllScope
    void gcAll();

    void inject(MoonSky moonSky);

    @ProtectInjected(timeMillis = 50)
    void protectInjected(MoonSky moonSky);
}
