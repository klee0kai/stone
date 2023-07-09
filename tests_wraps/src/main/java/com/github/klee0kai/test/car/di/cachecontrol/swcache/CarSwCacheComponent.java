package com.github.klee0kai.test.car.di.cachecontrol.swcache;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.test.car.di.cachecontrol.gc.*;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperRedScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope;

@Component(
        qualifiers = {String.class, Integer.class}
)
public abstract class CarSwCacheComponent {

    public abstract BumperGcModule bumpersModule();

    public abstract WheelGcModule wheelsModule();

    public abstract WindowGcModule windowsModule();

    public abstract WindowMappedGcModule windowsMappedModule();

    public abstract WindowMultiMappedGcModule windowsMultiMappedModule();

    @GcAllScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void allWeak();

    @GcWeakScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
    public abstract void weakToStrongFewMillis();

    @GcWeakScope
    @SwitchCache(cache = SwitchCache.CacheType.Soft, timeMillis = 100)
    public abstract void weakToSoftFewMillis();


    @GcSoftScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void softToWeak();

    @GcStrongScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void strongToWeak();

    @GcWheelScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void wheelsToWeak();

    @GcBumperScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void bumpersToWeak();

    @GcBumperRedScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void redBumpersToWeak();

    @GcBumperRedScope
    @GcBumperScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void redBumpersToWeak2();

    @GcWindowScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void windowsToWeak();

    @GcWindowScope
    @GcWeakScope
    @SwitchCache(cache = SwitchCache.CacheType.Strong)
    public abstract void weakWindowsToStrong();

    @GcWindowScope
    @GcSoftScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void softWindowsToWeak();

    @GcWindowScope
    @GcBumperScope
    @SwitchCache(cache = SwitchCache.CacheType.Weak)
    public abstract void nothingToWeak();

    public void windowsAndWheelsToWeak() {
        wheelsToWeak();
        windowsToWeak();
    }


}
