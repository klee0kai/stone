package com.github.klee0kai.test.inject.di;


import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.inject.identity.Conscience;
import com.github.klee0kai.test.inject.identity.Knowledge;


@Module
public interface IdentityModule {

    @Provide(cache = Provide.CacheType.Factory)
    public Knowledge knowledge();

    @Provide(cache = Provide.CacheType.Factory)
    public Conscience conscience();

}
