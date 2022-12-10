package com.github.klee0kai.test.cache.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.test.cache.di.ann.StoneStrongScope;


@Component
public interface CacheTestComponent {

    CacheDataModule data();

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void allWeak();


    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    void allStrongFewMillis();


    @StoneStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void strongToWeak();

}
