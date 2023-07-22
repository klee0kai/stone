package com.github.klee0kai.test.car.di.wrapped.custom;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.WeakReference;

@Module
public interface CarCustomWrapperModule {

    @Provide(cache = Provide.CacheType.Factory)
    Wheel whell();

    @Provide(cache = Provide.CacheType.Factory)
    WeakReference<Bumper> bumper();

    @Provide(cache = Provide.CacheType.Factory)
    Window window();

    @Provide(cache = Provide.CacheType.Factory)
    Car car(Bumper bumper, Wheel wheel, Window window);


}
