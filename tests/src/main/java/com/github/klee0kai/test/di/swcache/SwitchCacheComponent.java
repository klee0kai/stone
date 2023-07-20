package com.github.klee0kai.test.di.swcache;

import com.github.klee0kai.stone._hidden_.IPrivateComponent;
import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.test.di.gcforest.GcEarthModule;
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope;

@Component
public interface SwitchCacheComponent extends IPrivateComponent {

    GcEarthModule earth();

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void allWeak();


    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    void allStrongFewMillis();


    @GcStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void strongToWeak();

    @GcMountainScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void mountainToWeak();

}
