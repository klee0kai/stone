package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope;

public interface IGcEarthComponent {


    @GcMountainScope
    void gcMountains();

    @GcRiverScope
    void gcRivers();

    @GcMountainScope
    @GcRiverScope
    void gcMountainsAndRivers();
}
