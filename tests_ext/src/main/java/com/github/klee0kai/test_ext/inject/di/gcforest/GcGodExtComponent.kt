package com.github.klee0kai.test_ext.inject.di.gcforest

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.di.gcforest.GcGodComponent
import com.github.klee0kai.test.di.gcforest.scopes.GcPlanetScope
import com.github.klee0kai.test.di.gcforest.scopes.GcSunScope

@Component
abstract class GcGodExtComponent : GcGodComponent(), IGcEarthExtComponent {

    @ExtendOf
    abstract fun extOf(parent: GcGodComponent?)

    public abstract override fun sunSystem(): GcSunSystemExtModule?

    public abstract override fun earth(): GcEarthExtModule?

    @RunGc
    @GcAllScope
    abstract fun gcAllExt()

    @RunGc
    @GcStrongScope
    abstract fun gcStrongExt()

    @RunGc
    @GcSoftScope
    abstract fun gcSoftExt()

    @RunGc
    @GcWeakScope
    abstract fun gcWeakExt()


    @RunGc
    @GcSunScope
    abstract fun gcSunExt()

    @RunGc
    @GcPlanetScope
    abstract fun gcPlanetsExt()

}
