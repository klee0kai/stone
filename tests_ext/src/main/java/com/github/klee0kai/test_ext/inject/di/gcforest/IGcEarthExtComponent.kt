package com.github.klee0kai.test_ext.inject.di.gcforest;

import com.github.klee0kai.stone.annotations.component.GcSoftScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.RunGc;
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope;

public interface IGcEarthExtComponent {


    @RunGc
    @GcMountainScope
    void gcMountainsExt();

    @RunGc
    @GcSoftScope
    @GcMountainScope
    void gcSoftMountainsExt();

    @RunGc
    @GcStrongScope
    @GcMountainScope
    void gcStrongMountainsExt();

    @RunGc
    @GcRiverScope
    void gcRiversExt();

}
