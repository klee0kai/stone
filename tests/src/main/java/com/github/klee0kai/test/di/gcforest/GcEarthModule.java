package com.github.klee0kai.test.di.gcforest;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope;
import com.github.klee0kai.test.di.gcforest.scopes.GcRiverScope;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import com.github.klee0kai.test.mowgli.earth.River;

@Module
public abstract class GcEarthModule {

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Strong)
    abstract public Mountain mountainStrong();

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Soft)
    abstract public Mountain mountainSoft();

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Weak)
    abstract public Mountain mountainWeak();

    @GcMountainScope
    @Provide(cache = Provide.CacheType.Factory)
    public Mountain mountainFactory() {
        return new Mountain();
    }

    @GcMountainScope
    @Provide
    abstract public Mountain mountainDefaultSoft();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Strong)
    abstract public River riverStrong();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Soft)
    abstract public River riverSoft();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Weak)
    abstract public River riverWeak();

    @GcRiverScope
    @Provide(cache = Provide.CacheType.Factory)
    abstract public River riverFactory();

    @GcRiverScope
    @Provide
    public River riverDefaultSoft() {
        return new River();
    }


}
