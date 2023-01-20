package com.github.klee0kai.test.di.bindinstance.singlemethod;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;

@Component
public interface PlanetComponent extends IComponent {

    SunModule sunModule();

    @BindInstance
    IPlanet planet(IPlanet planet);

    @BindInstance(cache = BindInstance.CacheType.Weak)
    Earth earth(Earth earth);

    IPlanet providePlanet();


}
