package com.github.klee0kai.test.car.di.cachecontrol.gc

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope
import com.github.klee0kai.test.car.model.Window
import java.lang.ref.WeakReference
import java.util.*

@Module
open class WindowGcModule {
    @GcWindowScope
    @Provide(cache = Provide.CacheType.Factory)
    open fun windowFactory(): WeakReference<Collection<Window>> {
        return WeakReference(Arrays.asList(Window(), Window(), Window()))
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Weak)
    open fun windowWeak(): WeakReference<Collection<Window>> {
        return WeakReference(Arrays.asList(Window(), Window(), Window()))
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Soft)
    open fun windowSoft(): WeakReference<Collection<Window>> {
        return WeakReference(Arrays.asList(Window(), Window(), Window()))
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Strong)
    open fun windowStrong(): WeakReference<List<Window>> {
        return WeakReference(Arrays.asList(Window(), Window(), Window()))
    }
}
