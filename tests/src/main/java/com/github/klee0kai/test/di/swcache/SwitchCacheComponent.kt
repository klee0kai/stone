package com.github.klee0kai.test.di.swcache

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.GcAllScope
import com.github.klee0kai.stone.annotations.component.GcStrongScope
import com.github.klee0kai.stone.annotations.component.SwitchCache
import com.github.klee0kai.test.di.gcforest.GcEarthModule
import com.github.klee0kai.test.di.gcforest.scopes.GcMountainScope

@Component
interface SwitchCacheComponent {
    fun earth(): GcEarthModule?

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    fun allWeak()


    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    fun allStrongFewMillis()


    @GcStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    fun strongToWeak()

    @GcMountainScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    fun mountainToWeak()
}
