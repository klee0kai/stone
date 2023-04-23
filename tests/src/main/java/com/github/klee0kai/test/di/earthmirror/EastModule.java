package com.github.klee0kai.test.di.earthmirror;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.mowgli.earth.Cave;

@Module
public interface EastModule {

    @Provide(cache = Provide.CacheType.Soft)
    Cave cave();

    @Provide(cache = Provide.CacheType.Soft)
    Cave cave(Cave.CaveType type, Integer deep);

}
