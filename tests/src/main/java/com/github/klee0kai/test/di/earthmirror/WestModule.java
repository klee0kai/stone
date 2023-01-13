package com.github.klee0kai.test.di.earthmirror;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.earth.*;

@Module
public abstract class WestModule {

    @Provide(cache = Provide.CacheType.Factory)
    public abstract IRiver river();

    @Provide(cache = Provide.CacheType.Soft)
    public abstract IMountain mountain();

    @Provide(cache = Provide.CacheType.Factory)
    public IRiver riverImpl() {
        return new River();
    }

    @Provide(cache = Provide.CacheType.Soft)
    public IMountain mountainImp() {
        return new Mountain();
    }


    @Provide(cache = Provide.CacheType.Soft)
    public abstract Cave cave();

    @Provide(cache = Provide.CacheType.Soft)
    public abstract Cave cave(Cave.CaveType type, int deep);

    @Provide(cache = Provide.CacheType.Soft)
    public abstract Cave cave(Cave.CaveType type);


    @Provide(cache = Provide.CacheType.Soft)
    public abstract Cave cave(Cave.CaveType type, int deep, int space);

    @Provide(cache = Provide.CacheType.Soft)
    public abstract Cave cave(int deep, Cave.CaveType type);

}
