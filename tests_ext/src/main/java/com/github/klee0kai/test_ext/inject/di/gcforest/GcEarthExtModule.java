package com.github.klee0kai.test_ext.inject.di.gcforest;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.gcforest.GcEarthModule;
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope;
import com.github.klee0kai.test_ext.inject.mowgli.earth.Desert;
import com.github.klee0kai.test_ext.inject.mowgli.earth.WaterFlow;

@Module
public abstract class GcEarthExtModule extends GcEarthModule {

    @Provide(cache = Provide.CacheType.Strong)
    abstract public Desert desertStrong();

    @Provide(cache = Provide.CacheType.Soft)
    abstract public Desert desertSoft();

    @Provide(cache = Provide.CacheType.Weak)
    abstract public Desert desertWeak();

    @Provide(cache = Provide.CacheType.Factory)
    public Desert desertFactory() {
        return new Desert();
    }


    @GcRiverScope
    @Provide(cache = Provide.CacheType.Strong)
    abstract public WaterFlow riverStrong();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Soft)
    abstract public WaterFlow riverSoft();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Weak)
    abstract public WaterFlow riverWeak();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Factory)
    abstract public WaterFlow riverFactory();

    @GcRiverScope
    @Provide
    public WaterFlow riverDefaultSoft() {
        return new WaterFlow();
    }

}
