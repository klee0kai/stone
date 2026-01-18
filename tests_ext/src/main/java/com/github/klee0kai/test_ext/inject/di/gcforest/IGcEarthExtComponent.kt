package com.github.klee0kai.test_ext.inject.di.gcforest

import com.github.klee0kai.stone.annotations.component.GcSoftScope
import com.github.klee0kai.stone.annotations.component.GcStrongScope
import com.github.klee0kai.stone.annotations.component.RunGc
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope

interface IGcEarthExtComponent {

    @RunGc
    @GcMountainScope
    fun gcMountainsExt()

    @RunGc
    @GcSoftScope
    @GcMountainScope
    fun gcSoftMountainsExt()

    @RunGc
    @GcStrongScope
    @GcMountainScope
    fun gcStrongMountainsExt()

    @RunGc
    @GcRiverScope
    fun gcRiversExt()

}
