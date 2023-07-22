package com.github.klee0kai.test.car.di.cachecontrol.gc

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope
import com.github.klee0kai.test.car.model.Wheel
import java.lang.ref.WeakReference

@Module
interface WheelGcModule {
    @GcWheelScope
    @Provide(cache = Provide.CacheType.Factory)
    fun wheelFactory(): WeakReference<Wheel?>?

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Weak)
    fun wheelWeak(): WeakReference<Wheel?>?

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Soft)
    fun wheelSoft(): WeakReference<Wheel?>?

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Strong)
    fun wheelStrong(): WeakReference<Wheel?>?
}
