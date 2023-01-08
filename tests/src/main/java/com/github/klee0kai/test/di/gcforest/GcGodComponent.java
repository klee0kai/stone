package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;

@Component
public interface GcGodComponent extends IComponent, IGcEarthComponent {

    GcSunSystemModule sunSystem();

    GcEarthModule earth();

    @GcAllScope
    void gcAll();

    @GcStrongScope
    void gcStrong();

    @GcSoftScope
    void gcSoft();

    @GcWeakScope
    void gcWeak();


    @GcSunScope
    void gcSun();

    @GcPlanetScope
    void gcPlanets();

    @GcSunScope
    @GcPlanetScope
    void gcSunAndPlanets();

}
