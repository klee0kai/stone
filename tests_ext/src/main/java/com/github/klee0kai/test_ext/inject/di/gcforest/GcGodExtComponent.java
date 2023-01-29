package com.github.klee0kai.test_ext.inject.di.gcforest;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;

@Component
public abstract class GcGodExtComponent extends GcGodComponent implements IGcEarthExtComponent {

    @Override
    public abstract GcSunSystemExtModule sunSystem();

    @Override
    public abstract GcEarthExtModule earth();

    @GcAllScope
    public abstract void gcAllExt();

    @GcStrongScope
    public abstract void gcStrongExt();

    @GcSoftScope
    public abstract void gcSoftExt();

    @GcWeakScope
    public abstract void gcWeakExt();


    @GcSunScope
    public abstract void gcSunExt();

    @GcPlanetScope
    public abstract void gcPlanetsExt();

}
