package com.github.klee0kai.test.car.di.cachecontrol.swcache

import com.github.klee0kai.stone.annotations.component.*
import com.github.klee0kai.test.car.di.cachecontrol.gc.*
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperRedScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope

@Component(identifiers = [String::class, Integer::class])
abstract class CarSwCacheComponent {
    abstract fun bumpersModule(): BumperGcModule?
    abstract fun wheelsModule(): WheelGcModule?
    abstract fun windowsModule(): WindowGcModule?
    abstract fun windowsMappedModule(): WindowMappedGcModule?
    abstract fun windowsMultiMappedModule(): WindowMultiMappedGcModule?

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun allWeak()

    @GcWeakScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    abstract fun weakToStrongFewMillis()

    @GcWeakScope
    @SwitchCache(cache = SwitchCache.CacheType.Soft, timeMillis = 100)
    abstract fun weakToSoftFewMillis()

    @GcSoftScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun softToWeak()

    @GcStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun strongToWeak()

    @GcWheelScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun wheelsToWeak()

    @GcBumperScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun bumpersToWeak()

    @GcBumperRedScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun redBumpersToWeak()

    @GcBumperRedScope
    @GcBumperScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun redBumpersToWeak2()

    @GcWindowScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun windowsToWeak()

    @GcWindowScope
    @GcWeakScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong)
    abstract fun weakWindowsToStrong()

    @GcWindowScope
    @GcSoftScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun softWindowsToWeak()

    @GcWindowScope
    @GcBumperScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    abstract fun nothingToWeak()
    fun windowsAndWheelsToWeak() {
        wheelsToWeak()
        windowsToWeak()
    }
}
