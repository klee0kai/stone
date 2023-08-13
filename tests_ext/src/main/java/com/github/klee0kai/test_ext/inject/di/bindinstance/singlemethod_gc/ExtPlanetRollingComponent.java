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


    @RunGc
    @GcAllScope
    void gcAllExt();

    @RunGc
    @GcStrongScope
    void gcStrongExt();

    @RunGc
    @GcSoftScope
    void gcSoftExt();

    @RunGc
    @GcWeakScope
    void gcWeakExt();


    @RunGc
    @GcSoftScope
    @GcSunScope
    void gcSoftSunExt();

    @RunGc
    @GcSoftScope
    @GcPlanetScope
    void gcSoftPlanetsExt();


    @RunGc
    @GcSoftScope
    @GcSputnikScope
    void gcSoftSputniksExt();


    @RunGc
    @GcSoftScope
    @GcSiriusScope
    void gcSoftSiriusExt();


}
