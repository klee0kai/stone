package com.github.klee0kai.test.car.di.cachecontrol.gc;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope;
import com.github.klee0kai.test.car.model.Wheel;

import java.lang.ref.WeakReference;

@Module
public interface WheelGcModule {

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Factory)
    WeakReference<Wheel> wheelFactory();

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Weak)
    WeakReference<Wheel> wheelWeak();

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Soft)
    WeakReference<Wheel> wheelSoft();

    @GcWheelScope
    @Provide(cache = Provide.CacheType.Strong)
    WeakReference<Wheel> wheelStrong();


}
