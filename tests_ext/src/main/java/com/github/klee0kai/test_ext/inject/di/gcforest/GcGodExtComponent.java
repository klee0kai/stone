package com.github.klee0kai.test_ext.inject.di.gcforest;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope;

@Component
public abstract class GcGodExtComponent extends GcGodComponent implements IGcEarthExtComponent {


    @ExtendOf
    public abstract void extOf(GcGodComponent parent);

    @Override
    public abstract GcSunSystemExtModule sunSystem();

    @Override
    public abstract GcEarthExtModule earth();

    @RunGc
    @GcAllScope
    public abstract void gcAllExt();

    @RunGc
    @GcStrongScope
    public abstract void gcStrongExt();

    @RunGc
    @GcSoftScope
    public abstract void gcSoftExt();

    @RunGc
    @GcWeakScope
    public abstract void gcWeakExt();


    @RunGc
    @GcSunScope
    public abstract void gcSunExt();

    @RunGc
    @GcPlanetScope
    public abstract void gcPlanetsExt();

}
