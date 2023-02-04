package com.github.klee0kai.test.di.earthmirror;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.earth.Cave;
import com.github.klee0kai.test.mowgli.earth.IMountain;
import com.github.klee0kai.test.mowgli.earth.IRiver;

@Module
public interface EastModule {

    @Provide(cache = Provide.CacheType.Factory)
    IRiver river();

    @Provide(cache = Provide.CacheType.Soft)
    IMountain mountain();


    @Provide(cache = Provide.CacheType.Soft)
    Cave cave();

    @Provide(cache = Provide.CacheType.Soft)
    Cave cave(Cave.CaveType type, Integer deep);

    @Provide(cache = Provide.CacheType.Soft)
    Cave cave(Cave.CaveType type);


    @Provide(cache = Provide.CacheType.Soft)
    Cave cave(Cave.CaveType type, Integer deep, Integer space);

    @Provide(cache = Provide.CacheType.Soft)
    Cave cave(Integer deep, Cave.CaveType type);

}
