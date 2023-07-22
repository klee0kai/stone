package com.github.klee0kai.test.car.di.cachecontrol.gc;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Module
public class WindowMultiMappedGcModule {

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Factory)
    public WeakReference<Collection<Window>> windowFactory(Integer inx, String qualifier) {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Weak)
    public WeakReference<Collection<Window>> windowWeak(Integer inx, String qualifier) {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Soft)
    public WeakReference<Collection<Window>> windowSoft(Integer inx, String qualifier) {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }

    @GcWindowScope
    @Provide(cache = Provide.CacheType.Strong)
    public WeakReference<List<Window>> windowStrong(Integer inx, String qualifier) {
        return new WeakReference<>(Arrays.asList(new Window(), new Window(), new Window()));
    }


}
