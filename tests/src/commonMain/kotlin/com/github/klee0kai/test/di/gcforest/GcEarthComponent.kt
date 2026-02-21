package com.github.klee0kai.test.di.gcforest

import com.github.klee0kai.stone.annotations.component.GcSoftScope
import com.github.klee0kai.stone.annotations.component.GcStrongScope
import com.github.klee0kai.stone.annotations.component.RunGc
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope

abstract class GcEarthComponent {

    @RunGc
    @GcMountainScope
    abstract fun gcMountains()

    @RunGc
    @GcSoftScope
    @GcMountainScope
    abstract fun gcSoftMountains()

    @RunGc
    @GcStrongScope
    @GcMountainScope
    abstract fun gcStrongMountains()

    @RunGc
    @GcRiverScope
    abstract fun gcRivers()

    fun gcMountainsAndRivers() {
        gcMountains()
        gcRivers()
    }
}
