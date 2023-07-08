package com.github.klee0kai.test.car.di.cachecontrol.gc;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;

@Module
public class WindowGcModule {

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Factory)
    public WeakReference<Collection<Window>> windowFactory() {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Weak)
    public WeakReference<Collection<Window>> windowWeak() {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Soft)
    public WeakReference<Collection<Window>> windowSoft() {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Strong)
    public WeakReference<Collection<Window>> windowStrong() {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }


}
