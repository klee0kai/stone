package com.github.klee0kai.test.di.gcforest

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope
import com.github.klee0kai.test.mowgli.earth.Mountain
import com.github.klee0kai.test.mowgli.earth.River

@Module
abstract class GcEarthModule {

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Strong)
    abstract fun mountainStrong(): Mountain?

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Soft)
    abstract fun mountainSoft(): Mountain?

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Weak)
    abstract fun mountainWeak(): Mountain?

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Factory)
    open fun mountainFactory(): Mountain? {
        return Mountain()
    }

    @GcMountainScope
    @Provide
    abstract fun mountainDefaultFactory(): Mountain?

    @GcMountainScope
    abstract fun mountainDefault2Factory(): Mountain?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Strong)
    abstract fun riverStrong(): River?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Soft)
    abstract fun riverSoft(): River?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Weak)
    abstract fun riverWeak(): River?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Factory)
    abstract fun riverFactory(): River?

    @GcRiverScope
    @Provide
    open fun riverDefaultSoft(): River? {
        return River()
    }
}
