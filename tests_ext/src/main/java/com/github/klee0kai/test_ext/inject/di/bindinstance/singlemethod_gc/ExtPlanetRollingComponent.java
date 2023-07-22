package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod_gc;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.StarModule;
import com.github.klee0kai.test_ext.inject.di.gcscopes.GcSiriusScope;
import com.github.klee0kai.test_ext.inject.di.gcscopes.GcSputnikScope;
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.sputniks.Moon;

@Component
public interface ExtPlanetRollingComponent extends PlanetRollingComponent {

    @Override
    StarModule sunModule();

    @ExtendOf
    void extOf(PlanetRollingComponent ext);

    @GcSputnikScope
    @BindInstance(cache = BindInstance.CacheType.Strong)
    Moon moonStrong(Moon moon);

    @GcSputnikScope
    @BindInstance(cache = BindInstance.CacheType.Soft)
    Moon moonSoft(Moon moon);

    @GcSputnikScope
    @BindInstance(cache = BindInstance.CacheType.Weak)
    Moon moonWeak(Moon moon);


    @GcAllScope
    void gcAllExt();

    @GcStrongScope
    void gcStrongExt();

    @GcSoftScope
    void gcSoftExt();

    @GcWeakScope
    void gcWeakExt();


    @GcSoftScope
    @GcSunScope
    void gcSoftSunExt();

    @GcSoftScope
    @GcPlanetScope
    void gcSoftPlanetsExt();


    @GcSoftScope
    @GcSputnikScope
    void gcSoftSputniksExt();


    @GcSoftScope
    @GcSiriusScope
    void gcSoftSiriusExt();


}
