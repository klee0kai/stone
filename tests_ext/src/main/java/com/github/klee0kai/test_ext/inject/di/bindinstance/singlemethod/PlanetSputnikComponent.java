package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent;
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.sputniks.Moon;

@Component
public interface PlanetSputnikComponent extends PlanetComponent {

    @Override
    StarModule sunModule();

    @BindInstance(cache = BindInstance.CacheType.Weak)
    Moon moon(Moon moon);

}
