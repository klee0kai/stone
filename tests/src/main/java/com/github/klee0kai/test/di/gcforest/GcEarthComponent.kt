package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.stone.annotations.component.GcSoftScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.RunGc;
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope;

public abstract class GcEarthComponent {


    @RunGc
    @GcMountainScope
    public abstract void gcMountains();

    @RunGc
    @GcSoftScope
    @GcMountainScope
    public abstract void gcSoftMountains();

    @RunGc
    @GcStrongScope
    @GcMountainScope
    public abstract void gcStrongMountains();

    @RunGc
    @GcRiverScope
    public abstract void gcRivers();

    public void gcMountainsAndRivers() {
        gcMountains();
        gcRivers();
    }
}
