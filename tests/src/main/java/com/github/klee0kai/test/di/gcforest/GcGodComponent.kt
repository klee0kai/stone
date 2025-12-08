package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public abstract class GcGodComponent extends GcEarthComponent {

    public abstract GcSunSystemModule sunSystem();

    public abstract GcEarthModule earth();

    @BindInstance
    public abstract void bind(Sun sun);

    @BindInstance
    public abstract void bind(Earth earth);

    @BindInstance
    public abstract void bind(Saturn saturn);


    @RunGc
    @GcAllScope
    public abstract void gcAll();

    @RunGc
    @GcStrongScope
    public abstract void gcStrong();

    @RunGc
    @GcSoftScope
    public abstract void gcSoft();

    @RunGc
    @GcWeakScope
    public abstract void gcWeak();

    @RunGc
    @GcSunScope
    public abstract void gcSun();

    @RunGc
    @GcPlanetScope
    public abstract void gcPlanets();

    public void gcSunAndPlanets() {
        gcSun();
        gcPlanets();
    }

}
