package com.github.klee0kai.test.di.bindinstance.alternatives;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.galaxy.IPlanet;
import com.github.klee0kai.test.mowgli.galaxy.ISpaceObject;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;

@Component
public interface AlternativePlanetComponent {

    @BindInstance(cache = BindInstance.CacheType.Weak, alternatives = {IPlanet.class, ISpaceObject.class})
    Mercury mercury(Mercury earth);

    Mercury mercury();

    IPlanet planet();

    ISpaceObject spaceObject();

}
