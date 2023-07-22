package com.github.klee0kai.test.di.bindinstance.simple_inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;

@Component
public interface SevenPlanetComponent {


    SevenPlanetModule planets();

    StarsModule stars();

    @BindInstance
    void bind(Earth earth);

    @BindInstance
    void bind(Mercury mercury);

    @BindInstance
    void bindPlanet(IPlanet planet);

    void inject(MoonSky moonSky);
}
