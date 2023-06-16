package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.closed.IComponent;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;

@Component
public abstract class GcGodComponent extends GcEarthComponent implements IComponent {

    public abstract GcSunSystemModule sunSystem();

    public abstract GcEarthModule earth();

    @GcAllScope
    public abstract void gcAll();

    @GcStrongScope
    public abstract void gcStrong();

    @GcSoftScope
    public abstract void gcSoft();

    @GcWeakScope
    public abstract void gcWeak();

    @GcSunScope
    public abstract void gcSun();

    @GcPlanetScope
    public abstract void gcPlanets();

    public void gcSunAndPlanets() {
        gcSun();
        gcPlanets();
    }

}
