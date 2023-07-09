package com.github.klee0kai.test.car.di.cachecontrol.gc;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope;
import com.github.klee0kai.test.car.model.Wheel;

import java.lang.ref.WeakReference;

@Module
public class WheelMultiMappedGcModule {

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Factory)
    public WeakReference<Wheel> wheelFactory(String q1, Integer q2) {
        return new WeakReference<>(new Wheel());
    }

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Weak)
    public WeakReference<Wheel> wheelWeak(String q1, Integer q2) {
        return new WeakReference<>(new Wheel());
    }

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Soft)
    public WeakReference<Wheel> wheelSoft(String q1, Integer q2) {
        return new WeakReference<>(new Wheel());
    }

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Strong)
    public WeakReference<Wheel> wheelStrong(String q1, Integer q2) {
        return new WeakReference<>(new Wheel());
    }


}
