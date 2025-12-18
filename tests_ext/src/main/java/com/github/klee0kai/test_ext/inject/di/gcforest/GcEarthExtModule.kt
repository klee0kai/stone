package com.github.klee0kai.test_ext.inject.di.gcforest

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.di.gcforest.GcEarthModule
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope
import com.github.klee0kai.test_ext.inject.mowgli.earth.Desert
import com.github.klee0kai.test_ext.inject.mowgli.earth.WaterFlow

@Module
abstract class GcEarthExtModule : GcEarthModule() {

    @Provide(cache = Provide.CacheType.Strong)
    abstract fun desertStrong(): Desert?

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun desertSoft(): Desert?

    @Provide(cache = Provide.CacheType.Weak)
    abstract fun desertWeak(): Desert?

    @Provide(cache = Provide.CacheType.Factory)
    open fun desertFactory(): Desert {
        return Desert()
    }

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Strong)
    public abstract override fun riverStrong(): WaterFlow?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Soft)
    public abstract override fun riverSoft(): WaterFlow?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Weak)
    public abstract override fun riverWeak(): WaterFlow?

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Factory)
    public abstract override fun riverFactory(): WaterFlow?

    @GcRiverScope
    @Provide
    public override fun riverDefaultSoft(): WaterFlow? {
        return WaterFlow()
    }

}
