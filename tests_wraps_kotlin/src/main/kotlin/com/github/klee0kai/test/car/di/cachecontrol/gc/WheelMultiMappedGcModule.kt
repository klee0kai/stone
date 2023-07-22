package com.github.klee0kai.test.car.di.cachecontrol.gc

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope
import com.github.klee0kai.test.car.model.Wheel
import java.lang.ref.WeakReference

@Module
open class WheelMultiMappedGcModule {
    @GcWheelScope
    @Provide(cache = Provide.CacheType.Factory)
    open fun wheelFactory(q1: String?, q2: Integer?): WeakReference<Wheel> {
        return WeakReference(Wheel())
    }

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Weak)
    open fun wheelWeak(q1: String, q2: Integer): WeakReference<Wheel> {
        return WeakReference(Wheel())
    }

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Soft)
    open fun wheelSoft(q1: String, q2: Integer): WeakReference<Wheel> {
        return WeakReference(Wheel())
    }

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Strong)
    open fun wheelStrong(q1: String, q2: Integer?): WeakReference<Wheel> {
        return WeakReference(Wheel())
    }
}
