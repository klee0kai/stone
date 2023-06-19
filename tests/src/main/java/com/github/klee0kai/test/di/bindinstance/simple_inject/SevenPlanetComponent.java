package com.github.klee0kai.test.di.bindinstance.simple_inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.closed.IPrivateComponent;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;

@Component
public interface SevenPlanetComponent extends IPrivateComponent {


    SevenPlanetModule planets();

    StarsModule stars();

    @BindInstance
    void bindPlanet(IPlanet planet);

    void inject(MoonSky moonSky);
}
