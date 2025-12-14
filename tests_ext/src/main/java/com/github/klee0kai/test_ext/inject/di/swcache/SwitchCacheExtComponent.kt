package com.github.klee0kai.test_ext.inject.di.swcache

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.di.swcache.SwitchCacheComponent

@Component
interface SwitchCacheExtComponent : SwitchCacheComponent {

    @ExtendOf
    fun extOf(parent: SwitchCacheComponent?)

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    fun allWeakExt()

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    fun allStrongFewMillisExt()

    @GcStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    fun strongToWeakExt()

}
