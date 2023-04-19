package com.github.klee0kai.test_ext.inject.di.swcache;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.GcStrongScope;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.test.di.swcache.SwitchCacheComponent;

@Component
public interface SwitchCacheExtComponent extends SwitchCacheComponent {

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void allWeakExt();

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    void allStrongFewMillisExt();

    @GcStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    void strongToWeakExt();

}
