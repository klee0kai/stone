package com.github.klee0kai.wiki.provide.binding;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Module
public interface SunSystemModule {

    @BindInstance(cache = BindInstance.CacheType.Weak)
    Sun sun(Sun sun);

}