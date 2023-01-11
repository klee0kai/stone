package com.github.klee0kai.test.di.bindinstance;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public interface GodWorkspaceComponent extends IComponent {

    SunSystemModule sunSystem();

    @BindInstance
    void bindPlanet(IPlanet planet);

    @BindInstance
    void bindSun(Sun sun);

    @BindInstance
    void bindEarth(Earth earth);
}
